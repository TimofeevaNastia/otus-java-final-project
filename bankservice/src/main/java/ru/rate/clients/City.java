package ru.rate.clients;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public enum City {
    МОСКВА("moskva", "Москва"),
    НИЖНИЙ_НОВГОРОД("nizhniy-novgorod", "Нижний новгород"),
    ЧЕЛЯБИНСК("chelyabinsk", "Челябинск"),
    ПЕРМЬ("perm", "Пермь"),
    САМАРА("samara", "Самара");


    private final String value;
    private final String name;

    City(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public static String getValue(String nameCity) {
        return Arrays.stream(City.values()).filter(x ->
                StringUtils.containsIgnoreCase(nameCity, x.name)).findFirst().orElse(City.МОСКВА).value;
    }
}
