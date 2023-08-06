package ru.rate.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class BankRateResponse {
    List<BankRate> banks;
}
