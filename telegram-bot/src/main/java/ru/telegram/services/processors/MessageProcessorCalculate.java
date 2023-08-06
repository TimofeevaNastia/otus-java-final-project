package ru.telegram.services.processors;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import ru.telegram.model.getUpdates.GetUpdatesResponse;
import ru.telegram.model.sendMessage.Keyboard;
import ru.telegram.model.sendMessage.ReplyMarkup;
import ru.telegram.model.sendMessage.SendMessageRequest;
import ru.telegram.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service("messageProcessorCalculate")
@Slf4j
@AllArgsConstructor
public class MessageProcessorCalculate implements MessageProcessor {
    private final ApplicationContext applicationContext;
    private final UserRepository userRepository;

    @Override
    public SendMessageRequest process(GetUpdatesResponse.Message message) {
        log.info("process: {}", "messageProcessorCalculate");
        var replyMessage = StringUtils.equalsAny(message.getText(), Commands.CALCULATE.getMessage(), Commands.CALCULATE_ANOTHER_SUM.getMessage())
                ? Commands.ENTER_SUM.getMessage()
                : "Введен не правильный формат суммы.\n" + Commands.ENTER_SUM.getMessage();
        var pattern = Pattern.compile(Commands.GET_RATE.getMessage());
        var user = userRepository.findByUserId(message.getFrom().getId()).orElse(null);
        if (user != null) {
            if (user.getSelectedCurrency() == null) {
                var messageProcessor = applicationContext.getBean(Commands.START.getProcessor(), MessageProcessor.class);
                return messageProcessor.process(message);
            }
            var matcher = pattern.matcher(user.getSelectedCurrency());
            if (matcher.find()) {
                var action = matcher.group(1);
                var currency = matcher.group(2);
                replyMessage += " " + (action.equalsIgnoreCase("Купить") ? "RUB" : currency);
            }
        }
        var result = new SendMessageRequest();
        var keyboardList = new ArrayList<List<Keyboard>>();
        var keyboardSection = new ArrayList<Keyboard>();
        keyboardSection.add(new Keyboard().setText(Commands.ANOTHER_BANK.getMessage()));
        keyboardSection.add(new Keyboard().setText(Commands.ANOTHER_CURRENCY.getMessage()));
        keyboardList.add(keyboardSection);
        result.setText(replyMessage);
        result.setReplyMarkup(new ReplyMarkup()
                .setKeyboard(keyboardList)
                .setResizeKeyboard(true)
                .setOneTimeKeyboard(false));
        result.setChatId(message.getChat().getId());
        return result;
    }
}
