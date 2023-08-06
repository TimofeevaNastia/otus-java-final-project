package ru.telegram.clients;


import ru.rate.model.*;

public interface RateServiceClient {
    BankRateResponse getActualRate(String currencyCode);

    BankServiceResponse getBankAddressesWithCurrency(BankAddressRequest request);
}
