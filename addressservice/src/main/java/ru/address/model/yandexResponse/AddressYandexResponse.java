package ru.address.model.yandexResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.awt.*;
import java.util.List;

@Data
@Accessors(chain = true)
public class AddressYandexResponse {

    @JsonProperty("Components")
    private List<Components> components;
    private String formatted;
}
