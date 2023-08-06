package ru.telegram.services;

import lombok.extern.slf4j.Slf4j;
import ru.telegram.clients.TelegramClient;
import ru.telegram.model.getUpdates.GetUpdatesRequest;
import ru.telegram.model.getUpdates.GetUpdatesResponse;
import ru.telegram.services.processors.MessageSender;


@Slf4j
public class TelegramServiceImpl implements TelegramService {

    private final TelegramClient telegramClient;
    private final OffsetUpdater offsetId;
    private final MessageSender messageSender;

    public TelegramServiceImpl(TelegramClient telegramClient,
                               OffsetUpdater offsetId,
                               MessageSender messageSender) {
        this.telegramClient = telegramClient;
        this.offsetId = offsetId;
        this.messageSender = messageSender;
    }

    @Override
    public void process() {
        try {
            log.info("getUpdates begin");
            var offset = offsetId.getLast();
            var request = new GetUpdatesRequest(offset);
            var response = telegramClient.getUpdates(request);
            var lastUpdateId = processResponse(response);
            lastUpdateId = lastUpdateId == 0 ? offset : lastUpdateId + 1;
            offsetId.setLast(lastUpdateId);
            log.info("getUpdates end, lastUpdateId:{}", lastUpdateId);
        } catch (Exception ex) {
            log.error("unhandled exception", ex);
        }
    }


    private long processResponse(GetUpdatesResponse response) {
        log.info("response.getResult().size:{}", response.getResult().size());
        long lastUpdateId = 0;
        for (var responseMsg : response.getResult()) {
            lastUpdateId = Math.max(lastUpdateId, responseMsg.getUpdateId());
            messageSender.send(responseMsg.getMessage());
        }
        log.info("lastUpdateId:{}", lastUpdateId);
        return lastUpdateId;
    }


}
