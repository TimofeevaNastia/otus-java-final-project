package ru.telegram.services.processors;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import ru.rate.model.BankRateResponse;
import ru.telegram.clients.BankServiceClientImpl;
import ru.telegram.model.db.UserData;
import ru.telegram.model.getUpdates.GetUpdatesResponse;
import ru.telegram.model.sendMessage.Keyboard;
import ru.telegram.model.sendMessage.ReplyMarkup;
import ru.telegram.model.sendMessage.SendMessageRequest;
import ru.telegram.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service("messageProcessorRate")
@Slf4j
@AllArgsConstructor
public class MessageProcessorRate implements MessageProcessor {
    private final BankServiceClientImpl bankServiceClientImpl;
    private final ApplicationContext applicationContext;
    private final UserRepository userRepository;

    @Override
    public SendMessageRequest process(GetUpdatesResponse.Message message) {
        log.info("process: {}", "messageProcessorRate");
        var msgText = message.getText() != null ? message.getText() : message.getReplyToMessage().getText();
        var pattern = Pattern.compile(Commands.GET_RATE.getMessage());
        var matcher = pattern.matcher(msgText);
        var result = new SendMessageRequest();
        if (matcher.find()) {
            var action = matcher.group(1);
            var currency = matcher.group(2);
            var keyboardList = new ArrayList<List<Keyboard>>();
            var keyboardSection = new ArrayList<Keyboard>();
            keyboardSection.add(new Keyboard().setText(Commands.ANOTHER_CURRENCY.getMessage()));
            keyboardList.add(keyboardSection);
            BankRateResponse bankRate = bankServiceClientImpl.getActualRate(currency);
            result.setText(makeMessage(bankRate, action, currency));
            result.setChatId(message.getChat().getId());
            result.setReplyMarkup(new ReplyMarkup()
                    .setKeyboard(keyboardList)
                    .setResizeKeyboard(true)
                    .setOneTimeKeyboard(false));

            var user = userRepository.findByUserId(message.getFrom().getId()).orElse(null);
            var userRepId = user == null ? null : user.getId();
            var savedBank = user == null ? null : user.getSelectedBank();
            var rate = user == null ? null : user.getRate();
            userRepository.save(new UserData(userRepId, message.getFrom().getId(),
                    message.getChat().getId(),
                    savedBank,
                    msgText,
                    rate,
                    Commands.GET_RATE.getScenarioId()));

            return result;
        }
        if (msgText.equals(Commands.ANOTHER_BANK.getMessage())) {
            var user = userRepository.findByUserId(message.getFrom().getId()).orElse(null);
            if (user != null) {
                var messageProcessor = applicationContext.getBean(Commands.GET_RATE.getProcessor(), MessageProcessor.class);
                message.setText(user.getSelectedCurrency());
                return messageProcessor.process(message);
            }
        }
        var messageProcessor = applicationContext.getBean(Commands.START.getProcessor(), MessageProcessor.class);
        result = messageProcessor.process(message);
        return result;
    }

    private String makeMessage(BankRateResponse bankRate, String actionFromClient, String currency) {
        var text = """
                Выбранное действие: {action}
                                
                На текущий момент, наиболее выгодные курсы по вашему запросу предоставляют банки:
                                
                {bankInfo}
                Если вы хотите найти ближайший к вам филиал банка или рассчитать сумму после конвертации, выберите заинтересовавший вас банк.
                """;
        text = text.replaceFirst("\\{action}", actionFromClient + " " + currency);
        var doClientBuy = actionFromClient.equals("Купить");

        bankRate.setBanks(bankRate.getBanks().stream().sorted((o1, o2) -> {
            if (doClientBuy) return o1.getBuyByClientRate().compareTo(o2.getBuyByClientRate());
            else return o2.getSellByClientRate().compareTo(o1.getSellByClientRate());
        }).collect(Collectors.toList()));

        StringBuilder stringBuilder = new StringBuilder();
        bankRate.getBanks().forEach(bank -> {
            var rateValue = doClientBuy
                    ? bank.getBuyByClientRate()
                    : bank.getSellByClientRate();
            stringBuilder.append("  ☑️").append(bank.getNameBank())
                    .append(" /").append(bank.getCodeBank())
                    .append(" - ")
                    .append(rateValue).append(" р.").append("\n");
        });
        text = text.replaceFirst("\\{bankInfo}", stringBuilder.toString());
        return text;
    }
}
