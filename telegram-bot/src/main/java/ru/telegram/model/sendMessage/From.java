package ru.telegram.model.sendMessage;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class From {
    @JsonProperty("id")
    private long id;

    @JsonProperty("is_bot")
    private boolean isBot;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("username")
    private String username;
}
