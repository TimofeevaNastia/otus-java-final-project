package ru.telegram.services.processors;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.telegram.model.getUpdates.GetUpdatesResponse;
import ru.telegram.model.sendMessage.Keyboard;
import ru.telegram.model.sendMessage.ReplyMarkup;
import ru.telegram.model.sendMessage.SendMessageRequest;
import ru.telegram.repository.BankRepository;

import java.util.ArrayList;
import java.util.List;

@Service("messageProcessorBank")
@Slf4j
@AllArgsConstructor
public class MessageProcessorBank implements MessageProcessor {
    private final BankRepository bankRepository;

    @Override
    public SendMessageRequest process(GetUpdatesResponse.Message message) {
        log.info("process: {}", "messageProcessorBank");
        var msgText = message.getText() != null ? message.getText() : message.getReplyToMessage().getText();
        String nameBank = bankRepository.findNameBankByCode(msgText.replace("/", ""));
        var result = new SendMessageRequest();
        var keyboardList = new ArrayList<List<Keyboard>>();
        var keyboardSection1 = new ArrayList<Keyboard>();
        var keyboardSection2 = new ArrayList<Keyboard>();
        keyboardSection1.add(new Keyboard().setText(nameBank + ". " + Commands.SEARCH_OFFICE.getMessage()));
        keyboardSection2.add(new Keyboard().setText(Commands.CALCULATE.getMessage()));
        keyboardSection2.add(new Keyboard().setText(Commands.ANOTHER_CURRENCY.getMessage()));
        keyboardList.add(keyboardSection1);
        keyboardList.add(keyboardSection2);
        result.setText("Выберите действие в панели снизу");
        result.setReplyMarkup(new ReplyMarkup()
                .setKeyboard(keyboardList)
                .setResizeKeyboard(true)
                .setOneTimeKeyboard(false));
        result.setChatId(message.getChat().getId());
        return result;

    }

}
