package ru.rate.model.alfa;

import java.time.ZonedDateTime;

@lombok.Data
public class LastActualRate {
    private ZonedDateTime date;
    private Value buy;
    private Value sell;
}
