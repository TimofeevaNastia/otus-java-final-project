package ru.telegram.services.processors;

import ru.telegram.model.getUpdates.GetUpdatesResponse;
import ru.telegram.model.sendMessage.SendMessageRequest;

public interface MessageProcessor {
    SendMessageRequest process(GetUpdatesResponse.Message message);
}
