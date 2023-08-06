package ru.telegram.model.sendMessage;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Keyboard {
    @JsonProperty("text")
    private String text;

    @JsonProperty("request_location")
    private boolean requestLocation;
}
