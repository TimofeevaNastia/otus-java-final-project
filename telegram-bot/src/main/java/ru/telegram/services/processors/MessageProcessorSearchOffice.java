package ru.telegram.services.processors;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.telegram.model.getUpdates.GetUpdatesResponse;
import ru.telegram.model.sendMessage.Keyboard;
import ru.telegram.model.sendMessage.ReplyMarkup;
import ru.telegram.model.sendMessage.SendMessageRequest;

import java.util.ArrayList;
import java.util.List;

@Service("messageProcessorSearchOffice")
@Slf4j
@AllArgsConstructor
public class MessageProcessorSearchOffice implements MessageProcessor {

    @Override
    public SendMessageRequest process(GetUpdatesResponse.Message message) {
        log.info("process: {}", "messageProcessorSearchOffice");
        var result = new SendMessageRequest();
        var keyboardList = new ArrayList<List<Keyboard>>();
        var keyboardSection1 = new ArrayList<Keyboard>();
        var keyboardSection2 = new ArrayList<Keyboard>();
        keyboardSection1.add(new Keyboard().setText(Commands.SEND_LOCATION.getMessage()).setRequestLocation(true));
        keyboardSection2.add(new Keyboard().setText(Commands.ANOTHER_CURRENCY.getMessage()));
        keyboardList.add(keyboardSection1);
        keyboardList.add(keyboardSection2);
        result.setText("Введите адрес в текстовом поле в формате: город, улица, дом\nили нажмите кнопку отправить местоположение");
        result.setReplyMarkup(new ReplyMarkup()
                .setKeyboard(keyboardList)
                .setResizeKeyboard(true)
                .setOneTimeKeyboard(false)
                .setInputFieldPlaceholder("город, улица, дом"));
        result.setChatId(message.getChat().getId());
        return result;
    }
}
