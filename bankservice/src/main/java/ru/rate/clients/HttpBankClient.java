package ru.rate.clients;

import java.util.List;


public interface HttpBankClient {

    String getRate(String currency);

    List<String> getOfficesWithCurrency(String city);
}
