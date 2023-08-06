package ru.rate.model.sber;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Rates {
    @JsonProperty("EUR")
    private Currency EUR;
    @JsonProperty("USD")
    private Currency USD;
}
