package ru.rate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.rate.model.db.Addresses;
import ru.rate.model.db.Bank;
import ru.rate.repository.BankRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class BankServiceDbImpl implements BankServiceDb {
    private final BankRepository bankRepository;

    public BankServiceDbImpl(BankRepository bankRepository) {
        this.bankRepository = bankRepository;
    }

    @Override
    public List<Bank> getActualBanks() {
        var bankList = new ArrayList<Bank>();
        bankRepository.findAll().forEach(bankList::add);
        log.info("bankList:{}", bankList);
        return bankList;
    }

    @Override
    public List<Addresses> getAddressBankByCity(String codeBank, String city) {
        city = "%" + city + "%";
        return bankRepository.findAddressesOfBankByCity(codeBank, city);
    }

    @Override
    public Bank saveAddressBank(Bank bank) {
        return bankRepository.save(bank);
    }
}
