package ru.rate.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class Address {
    private String city;
    private String street;
    private String house;
    private String addressString;
    private BigDecimal latitude;
    private BigDecimal longitude;
}
