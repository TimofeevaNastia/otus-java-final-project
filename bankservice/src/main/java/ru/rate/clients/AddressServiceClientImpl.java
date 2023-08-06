package ru.rate.clients;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.rate.config.AddressUrlConfig;
import ru.rate.model.Address;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpRequest;
import java.util.List;

@AllArgsConstructor
@Service
@Slf4j
public class AddressServiceClientImpl extends BaseRequest implements AddressServiceClient {
    private final ObjectMapper objectMapper;
    private final AddressUrlConfig addressUrlConfig;
    private final String GET_NEAREST_ADDRESSES = "/getNearestAddresses";
    private final String GET_ADDRESS_STRING = "/getAddress";


    @Override
    public List<String> getNearestAddresses(Address currentAddress, List<Address> addresses) {
        try {
            addresses.add(currentAddress);
            var body = objectMapper.writeValueAsString(addresses);
            var response = performRequest(addressUrlConfig.getUrl() + GET_NEAREST_ADDRESSES, body);
            log.info("getNearestAddresses, response{}", response);
            return objectMapper.readValue(response, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new HttpClientException(e.getMessage());
        }
    }

    @Override
    public Address getCoordinatesAddresses(BigDecimal lat, BigDecimal lon) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(makeUrlGetAddress(lat, lon)))
                .GET()
                .build();
        var response = doRequest(request);
        try {
            return objectMapper.readValue(response, Address.class);
        } catch (JsonProcessingException e) {
            throw new HttpClientException(e.getMessage());
        }
    }

    private String makeUrlGetAddress(BigDecimal lat, BigDecimal lon) {
        return "%s%s/%s+%s".formatted(addressUrlConfig.getUrl(), GET_ADDRESS_STRING, lat, lon);
    }
}
