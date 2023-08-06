package ru.rate.model.sber;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class Geometry {
    private List<BigDecimal> coordinates;
}
