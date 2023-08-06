package ru.telegram.clients;


import ru.rate.model.*;

public interface BankServiceClient {
    BankRateResponse getActualRate(String currencyCode);

    BankServiceResponse getBankAddressesWithCurrency(BankAddressRequest request);
}
