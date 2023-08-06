package ru.rate.model.alfa;

import java.util.List;

@lombok.Data
public class Data {
    private String currencyCode;
    private List<RateByClientType> rateByClientType;
}
