package ru.rate.model.sber;

import lombok.Data;

import java.util.List;

@Data
public class Currency {
    private long startDateTime;
    private double lotSize;
    private List<Rate> rateList;
}
