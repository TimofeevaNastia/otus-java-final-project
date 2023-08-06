package ru.rate.model.alfa;

import java.util.List;

@lombok.Data
public class RatesByType {
    private String rateType;
    private List<String> ratesForPeriod;
    private LastActualRate lastActualRate;
}
