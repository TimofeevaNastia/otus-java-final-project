package ru.telegram.services.processors;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.ehcache.Cache;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import ru.rate.model.BankRateResponse;
import ru.telegram.clients.BankServiceClientImpl;
import ru.telegram.model.getUpdates.GetUpdatesResponse;
import ru.telegram.model.sendMessage.Keyboard;
import ru.telegram.model.sendMessage.ReplyMarkup;
import ru.telegram.model.sendMessage.SendMessageRequest;
import ru.telegram.repository.UserRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service("messageProcessorCalculateSum")
@Slf4j
@AllArgsConstructor
public class MessageProcessorCalculateSum implements MessageProcessor {
    private final Cache<LocalDate, BankRateResponse> currencyRateCache;
    private final BankServiceClientImpl bankServiceClientImpl;
    private final ApplicationContext applicationContext;
    private final UserRepository userRepository;

    @Override
    public SendMessageRequest process(GetUpdatesResponse.Message message) {
        log.info("process: {}", "messageProcessorCalculateSum");
        var msgText = message.getText() != null ? message.getText() : message.getReplyToMessage().getText();
        var user = userRepository.findByUserId(message.getFrom().getId()).orElse(null);
        if (user == null) {
            var messageProcessor = applicationContext.getBean(Commands.START.getProcessor(), MessageProcessor.class);
            return messageProcessor.process(message);
        }
        msgText = msgText.replaceAll(" ", "");
        if (validateSum(msgText)) {
            var result = new SendMessageRequest();
            var pattern = Pattern.compile(Commands.GET_RATE.getMessage());
            var matcher = pattern.matcher(user.getSelectedCurrency());
            if (matcher.find()) {
                var currency = matcher.group(2);
                var bankRateResponse = currencyRateCache.get(LocalDate.now());
                if (bankRateResponse == null) {
                    bankRateResponse = bankServiceClientImpl.getActualRate(currency);
                    currencyRateCache.put(LocalDate.now(), bankRateResponse);
                }

                var selectedBank = bankRateResponse.getBanks().stream()
                        .filter(x -> x.getCodeBank().equals(user.getSelectedBank()))
                        .findFirst().orElse(null);
                if (selectedBank == null) {
                    var messageProcessor = applicationContext.getBean(Commands.START.getProcessor(), MessageProcessor.class);
                    return messageProcessor.process(message);
                }

                var rate = new BigDecimal(0);
                var sum = new BigDecimal(msgText);
                var process = "Рассчитанная сумма:\n";
                var calculatedSum = new BigDecimal(0);

                var action = matcher.group(1);
                if (action.equalsIgnoreCase("Купить")) {
                    rate = selectedBank.getBuyByClientRate();
                    calculatedSum = sum.divide(rate, 2, RoundingMode.CEILING);
                    process = process + "%s RUB : %s = %s " + currency;
                } else {
                    rate = selectedBank.getSellByClientRate();
                    calculatedSum = sum.multiply(rate);
                    process = process + "%s " + currency + " * %s = %s " + "RUB";
                }

                var userRepId = user.getId();
                userRepository.updateUserScenarioId(userRepId, Commands.GET_RATE.getScenarioId());
                userRepository.updateUserRate(userRepId, rate);
                result.setText(String.format(process, getFormattedNumber(sum), rate, getFormattedNumber(calculatedSum)));
            }

            var keyboardList = new ArrayList<List<Keyboard>>();
            var keyboardSection1 = new ArrayList<Keyboard>();
            var keyboardSection2 = new ArrayList<Keyboard>();
            keyboardSection1.add(new Keyboard().setText(Commands.CALCULATE_ANOTHER_SUM.getMessage()));
            keyboardSection2.add(new Keyboard().setText(Commands.ANOTHER_CURRENCY.getMessage()));
            keyboardSection2.add(new Keyboard().setText(Commands.ANOTHER_BANK.getMessage()));
            keyboardList.add(keyboardSection1);
            keyboardList.add(keyboardSection2);
            result.setReplyMarkup(new ReplyMarkup()
                    .setKeyboard(keyboardList)
                    .setResizeKeyboard(true)
                    .setOneTimeKeyboard(false));
            result.setChatId(message.getChat().getId());
            return result;
        }
        var messageProcessor = applicationContext.getBean(Commands.CALCULATE.getProcessor(), MessageProcessor.class);
        return messageProcessor.process(message);
    }


    private boolean validateSum(String sum) {
        return StringUtils.isNumeric(sum);
    }

    private String getFormattedNumber(BigDecimal sumString) {
        return new DecimalFormat("###,###.##").format(sumString);
    }
}
