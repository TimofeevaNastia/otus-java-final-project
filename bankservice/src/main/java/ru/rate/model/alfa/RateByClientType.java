package ru.rate.model.alfa;

import java.util.List;

@lombok.Data
public class RateByClientType {
    private String clientType;
    private List<RatesByType> ratesByType;
}
