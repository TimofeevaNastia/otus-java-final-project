package ru.telegram.clients;


import ru.telegram.model.getUpdates.GetUpdatesRequest;
import ru.telegram.model.getUpdates.GetUpdatesResponse;
import ru.telegram.model.sendMessage.SendMessageRequest;
import ru.telegram.model.sendMessage.SendMessageResponse;

public interface TelegramClient {

    GetUpdatesResponse getUpdates(GetUpdatesRequest request);

    SendMessageResponse sendMessage(SendMessageRequest request);
}
