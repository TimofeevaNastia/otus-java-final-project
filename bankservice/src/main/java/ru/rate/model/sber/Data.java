package ru.rate.model.sber;


@lombok.Data
public class Data {
    private long id;
    private Geometry geometry;
    private Rates rates;
    private Properties properties;
    private String type;
}
