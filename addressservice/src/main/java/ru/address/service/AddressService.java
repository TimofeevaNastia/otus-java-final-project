package ru.address.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.ehcache.Cache;
import org.springframework.stereotype.Service;
import ru.address.clients.HttpClient;
import ru.address.model.yandexResponse.YandexResponse;
import ru.address.utils.Point;
import ru.rate.model.Address;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AddressService {
    private final ObjectMapper objectMapper;
    private final HttpClient yandexClient;
    private final Cache<String, String> cityCache;
    private int MAX_ADDRESSES = 5;

    public List<String> getNearestAddresses(Address currentAddress, List<Address> addresses) {
        double lonCur = currentAddress.getLongitude() == null ? 0 : currentAddress.getLongitude().doubleValue();
        double latCur = currentAddress.getLatitude() == null ? 0 : currentAddress.getLatitude().doubleValue();
        var currentAddressString = currentAddress.getAddressString();
        if (currentAddressString != null) {
            var currentPos = getPosition(currentAddressString);
            lonCur = currentPos.getLeft();
            latCur = currentPos.getRight();
            log.info("current position: {} {}", lonCur, latCur);
        }
        var distances = new ArrayList<NearestAddresses>();
        for (Address address : addresses) {
            var pos = getPosition(address.getAddressString());
            double lon = pos.getLeft();
            double lat = pos.getRight();
            Point pointFrom = new Point(lonCur, latCur);
            Point pointTo = new Point(lon, lat);
            distances.add(new NearestAddresses().setAddress(address.getAddressString()).setDistance(pointFrom.getDistance(pointTo)));

        }
        MAX_ADDRESSES = Math.min(distances.size(), MAX_ADDRESSES);
        return distances.stream().sorted(Comparator.comparingDouble(NearestAddresses::getDistance))
                .map(NearestAddresses::getAddress)
                .collect(Collectors.toList()).subList(0, MAX_ADDRESSES);

    }

    public Address getAddressByCoordinates(double lon, double lat) {
        try {
            log.info("получение адреса по координатам, долгота: {}, широта: {}", lon, lat);
            var response = yandexClient.getAddressData(lon + "+" + lat);
            var currentCo = objectMapper.readValue(response, YandexResponse.class);
            var featureMembers = currentCo.getResponse().getGeoObjectCollection().getFeatureMember();
            var featureMember = featureMembers.stream().filter(x -> x.getGeoObject().getMetaDataProperty().getGeocoderMetaData().getPrecision().equals("exact"))
                    .findFirst()
                    .orElseThrow(() -> new AddresstException("Не удалось определить адрес по текущему местоположению"));

            var component = featureMember.getGeoObject().getMetaDataProperty().getGeocoderMetaData().getAddress().getComponents().stream()
                    .filter(x -> x.getKind().equals("locality"))
                    .findFirst()
                    .orElseThrow(() -> new AddresstException("Не удалось определить город по текущему местоположению"));
            var city = component.getName();
            log.info("получен город: {}", city);
            return new Address().setCity(city).setLongitude(new BigDecimal(lon)).setLatitude(new BigDecimal(lat));
        } catch (JsonProcessingException e) {
            throw new ErrorFormatException(e);
        }
    }

    private String getFormattedAddress(String address) {
        return address.replaceAll(" ", "+");
    }

    private Pair<Double, Double> getPosition(String address) {
        double lonCur;
        double latCur;
        try {
            var pos = cityCache.get(address);
            if (pos == null || pos.isEmpty()) {
                var response = yandexClient.getAddressData(getFormattedAddress(address));
                var currentCo = objectMapper.readValue(response, YandexResponse.class);
                pos = currentCo.getResponse().getGeoObjectCollection().getFeatureMember().get(0).getGeoObject().getPoint().getPos();
                cityCache.put(address, pos);
            }
            String[] posArray = pos.split(" ");
            lonCur = Double.parseDouble(posArray[0]);
            latCur = Double.parseDouble(posArray[1]);
        } catch (JsonProcessingException e) {
            throw new ErrorFormatException(e);
        }
        return new ImmutablePair<>(lonCur, latCur);
    }
}
