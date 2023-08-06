package ru.telegram.model.sendMessage;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class ReplyMarkup {
    @JsonProperty("keyboard")
    private List<List<Keyboard>> keyboard;

    @JsonProperty("one_time_keyboard")
    private boolean oneTimeKeyboard;

    @JsonProperty("resize_keyboard")
    private boolean resizeKeyboard;

    @JsonProperty("input_field_placeholder")
    private String inputFieldPlaceholder;

    @JsonProperty("is_persistent")
    private boolean isPersistent;
}
