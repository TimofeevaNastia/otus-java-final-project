package ru.rate.clients;

import ru.rate.model.BankRate;

import java.util.List;

public interface Bank {
    BankRate getRate(String currency);

    List<String> getOfficesWithCurrency(String city);
}
