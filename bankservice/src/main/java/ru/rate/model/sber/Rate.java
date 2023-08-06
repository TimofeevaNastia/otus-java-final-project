package ru.rate.model.sber;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Rate {
    private int rangeAmountBottom;
    private BigDecimal rangeAmountUpper;
    private BigDecimal rateSell;
    private BigDecimal rateBuy;


//    private String symbolSell;
//    private String symbolBuy;
//
//    private long startDateTime;
//    private int lotSize;
//    private List<Rate> rateList;
}
