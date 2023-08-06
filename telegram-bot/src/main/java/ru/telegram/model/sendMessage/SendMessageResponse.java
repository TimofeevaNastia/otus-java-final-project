package ru.telegram.model.sendMessage;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SendMessageResponse {
    @JsonProperty("ok")
    private boolean ok;

    @JsonProperty("result")
    private Result result;
}
