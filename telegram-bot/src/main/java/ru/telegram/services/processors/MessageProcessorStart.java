package ru.telegram.services.processors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.telegram.model.getUpdates.GetUpdatesResponse;
import ru.telegram.model.sendMessage.Keyboard;
import ru.telegram.model.sendMessage.ReplyMarkup;
import ru.telegram.model.sendMessage.SendMessageRequest;

import java.util.ArrayList;
import java.util.List;

@Service("messageProcessorStart")
@Slf4j
public class MessageProcessorStart implements MessageProcessor {

    @Override
    public SendMessageRequest process(GetUpdatesResponse.Message message) {
        log.info("process: {}", "messageProcessorStart");
        var result = new SendMessageRequest();
        result.setText("Выберите один из вариантов");
        var keyboardList = new ArrayList<List<Keyboard>>();
        var keyboardSection1 = new ArrayList<Keyboard>();
        var keyboardSection2 = new ArrayList<Keyboard>();
        keyboardSection1.add(new Keyboard().setText("Купить EUR"));
        keyboardSection1.add(new Keyboard().setText("Купить USD"));
        keyboardSection2.add(new Keyboard().setText("Продать EUR"));
        keyboardSection2.add(new Keyboard().setText("Продать USD"));
        keyboardList.add(keyboardSection1);
        keyboardList.add(keyboardSection2);
        result.setReplyMarkup(new ReplyMarkup()
                .setKeyboard(keyboardList)
                .setResizeKeyboard(true)
                .setOneTimeKeyboard(false));
        result.setChatId(message.getChat().getId());
        return result;
    }
}
