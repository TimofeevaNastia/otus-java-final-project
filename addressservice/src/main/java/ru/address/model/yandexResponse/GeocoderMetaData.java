package ru.address.model.yandexResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GeocoderMetaData {
    private String precision;
    private String text;
    private String kind;
    @JsonProperty("Address")
    private AddressYandexResponse address;
}
