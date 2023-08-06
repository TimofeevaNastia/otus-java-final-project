package ru.address.model.yandexResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ResponseData {

    @JsonProperty("GeoObjectCollection")
    GeoObjectCollection geoObjectCollection;
}
