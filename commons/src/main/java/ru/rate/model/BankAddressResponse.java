package ru.rate.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class BankAddressResponse {
    private String nameBank;
    private String codeBank;
    private List<BankAddresses> addresses;
}
