package ru.address.model.yandexResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GeoObject {
    private MetaDataProperty metaDataProperty;
    private String name;
    private String description;
    @JsonProperty("Point")
    private Point point;
}
