package ru.address.service;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class NearestAddresses {
    private double distance;
    private String address;
}
