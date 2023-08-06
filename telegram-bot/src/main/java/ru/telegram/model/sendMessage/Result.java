package ru.telegram.model.sendMessage;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Result {

    @JsonProperty("message_id")
    private long messageId;

    @JsonProperty("from")
    private From from;

    @JsonProperty("chat")
    private Chat chat;

    @JsonProperty("date")
    private long date;

    @JsonProperty("text")
    private String text;

}
