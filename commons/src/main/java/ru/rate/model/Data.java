package ru.rate.model;

import lombok.experimental.Accessors;

@lombok.Data
@Accessors(chain = true)
public class Data {
    private BankAddressResponse bankServiceAddressesResponse;
}
