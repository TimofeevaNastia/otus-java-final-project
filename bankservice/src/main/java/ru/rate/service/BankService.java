package ru.rate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ehcache.Cache;
import org.springframework.stereotype.Service;
import ru.rate.clients.AddressServiceClient;
import ru.rate.clients.Bank;
import ru.rate.clients.BankFactory;
import ru.rate.model.Address;
import ru.rate.model.BankAddressRequest;
import ru.rate.model.BankRate;
import ru.rate.model.BankRateResponse;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BankService {
    private final BankServiceDb bankServiceDb;
    private final Cache<LocalDate, BankRateResponse> currencyRateCache;
    private final BankFactory bankFactory;
    private final AddressServiceClient addressService;

    public BankRateResponse getCurrencyRate(String currency) {
        var bankRateResponse = currencyRateCache.get(LocalDate.now());
        if (bankRateResponse == null) {
            bankRateResponse = new BankRateResponse();
            bankRateResponse.setBanks(new ArrayList<>());
            BankRateResponse finalBankRateResponse = bankRateResponse;
            bankServiceDb.getActualBanks().forEach(x -> {
                var bank = bankFactory.getBank(x.getCode());
                finalBankRateResponse.getBanks()
                        .add(bank.getRate(currency));
            });
            currencyRateCache.put(LocalDate.now(), bankRateResponse);
        }
        return bankRateResponse;
    }

    public BankRate getBankData(String bankCode, String currency) {
        return getCurrencyRate(currency).getBanks().stream()
                .filter(x -> x.getCodeBank().equals(bankCode))
                .findFirst()
                .orElseThrow(() -> new BankServiceException("Не найден банк по коду " + bankCode));
    }

    public List<String> getNearestBankAddresses(BankAddressRequest request) {
        var addressCurrent = getAddressFromString(request);
        var bank = bankFactory.getBank(request.getCodeBank());
        var cityCurrent = addressCurrent.getCity();
        if (cityCurrent == null) {
            cityCurrent = addressService.getCoordinatesAddresses(addressCurrent.getLatitude(), addressCurrent.getLongitude()).getCity();
            addressCurrent.setCity(cityCurrent);
        }
        var addresses = getActualAddressesFromBank(bank, cityCurrent);
        return addressService.getNearestAddresses(addressCurrent, addresses);
    }

    private Address getAddressFromString(BankAddressRequest request) {
        var address = request.getAddressString();
        if (request.getLatitude() == null) {
            var data = address.split(",");
            if (data.length < 2) {
                throw new AddressValidationException("Должен быть указан адрес в формате: город, улица, дом");
            }
            var city = data[0];
            var street = data[1];
            var house = data.length == 3 ? data[2] : null;
            // TODO: сделать нормальную валидацию адреса
//        Pattern pattern = Pattern.compile("г. ,");
//        Matcher matcher = pattern.matcher(address);
            return new Address().setCity(city).setStreet(street).setHouse(house).setAddressString(address);
        }
        return new Address().setLatitude(request.getLatitude()).setLongitude(request.getLongitude());
    }

    private List<Address> getActualAddressesFromBank(Bank bank, String city) {
        var addressesResult = new ArrayList<Address>();
        var addresses = bank.getOfficesWithCurrency(city);
        addresses.forEach(addr -> {
            String[] data = addr.split(",");
            var cityAddr = data[0];
            var street = data[1];
            var house = data[2];
            addressesResult.add(new Address().setCity(cityAddr).setStreet(street).setHouse(house).setAddressString(addr));
        });
        return addressesResult;
    }
}
