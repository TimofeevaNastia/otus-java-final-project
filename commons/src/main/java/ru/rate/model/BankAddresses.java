package ru.rate.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class BankAddresses {
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String addressString;



}
