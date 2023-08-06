package ru.telegram.model.sendMessage;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Entities {
    @JsonProperty("type")
    private TypeEntity type;

    @JsonProperty("offset")
    private long offset;

    @JsonProperty("length")
    private long length;
}
