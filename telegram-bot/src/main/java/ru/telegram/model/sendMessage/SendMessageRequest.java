package ru.telegram.model.sendMessage;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class SendMessageRequest {
    @JsonProperty("chat_id")
    private long chatId;

    @JsonProperty("text")
    private String text;

    @JsonProperty("entities")
    private List<Entities> entities;

    @JsonProperty("reply_markup")
    private ReplyMarkup replyMarkup;
}
