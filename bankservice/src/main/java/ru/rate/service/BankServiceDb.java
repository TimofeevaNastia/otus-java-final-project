package ru.rate.service;

import ru.rate.model.db.Addresses;
import ru.rate.model.db.Bank;

import java.util.List;

public interface BankServiceDb {
    List<Bank> getActualBanks();
    List<Addresses> getAddressBankByCity(String codeBank, String city);
    Bank saveAddressBank(Bank bank);
}
