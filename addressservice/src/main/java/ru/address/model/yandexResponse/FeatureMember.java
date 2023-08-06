package ru.address.model.yandexResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class FeatureMember {
    @JsonProperty("GeoObject")
    private GeoObject geoObject;
}
