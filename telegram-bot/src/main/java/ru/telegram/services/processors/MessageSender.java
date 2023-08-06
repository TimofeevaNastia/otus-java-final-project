package ru.telegram.services.processors;

import ru.telegram.model.getUpdates.GetUpdatesResponse;
import ru.telegram.model.sendMessage.SendMessageResponse;

public interface MessageSender {
    SendMessageResponse send(GetUpdatesResponse.Message message);
}
