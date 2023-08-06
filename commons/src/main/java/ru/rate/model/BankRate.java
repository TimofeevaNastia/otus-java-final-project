package ru.rate.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class BankRate {
    private String nameBank;
    private String codeBank;
    private BigDecimal buyByClientRate;
    private BigDecimal sellByClientRate;
}
