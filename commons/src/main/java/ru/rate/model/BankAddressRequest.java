package ru.rate.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class BankAddressRequest {
    private String nameBank;
    private String codeBank;
    private String addressString;
    private BigDecimal latitude;
    private BigDecimal longitude;
}
