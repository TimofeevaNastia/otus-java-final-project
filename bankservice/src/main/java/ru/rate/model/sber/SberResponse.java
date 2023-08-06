package ru.rate.model.sber;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class SberResponse {
    private List<ru.rate.model.sber.Data> data;

}
