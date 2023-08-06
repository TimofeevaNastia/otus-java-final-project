package ru.rate.clients;


import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;


public interface HttpBankClient {

    String getRate(String currency);

    List<String> getOfficesWithCurrency();
}
