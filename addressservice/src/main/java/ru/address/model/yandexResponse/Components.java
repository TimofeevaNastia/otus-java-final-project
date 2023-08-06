package ru.address.model.yandexResponse;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Components {
    private String kind;
    private String name;
}
