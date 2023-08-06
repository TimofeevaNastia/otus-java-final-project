package ru.telegram.services.processors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.telegram.clients.TelegramClient;
import ru.telegram.model.getUpdates.GetUpdatesResponse;
import ru.telegram.model.sendMessage.SendMessageResponse;

@Service
@Slf4j
public class MessageSenderImpl implements MessageSender {
    private final MessageProcessor messageProcessor;
    private final TelegramClient telegramClient;

    public MessageSenderImpl(@Qualifier("messageProcessor")
                                     MessageProcessor messageProcessor,
                             TelegramClient telegramClient) {
        this.messageProcessor = messageProcessor;
        this.telegramClient = telegramClient;
    }

    @Override
    public SendMessageResponse send(GetUpdatesResponse.Message message) {
        var request = messageProcessor.process(message);
        return telegramClient.sendMessage(request);
    }


}
