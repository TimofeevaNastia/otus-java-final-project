package ru.telegram.services.processors;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import ru.rate.model.BankAddressRequest;
import ru.telegram.clients.RateServiceClientImpl;
import ru.telegram.model.getUpdates.GetUpdatesResponse;
import ru.telegram.model.sendMessage.Keyboard;
import ru.telegram.model.sendMessage.ReplyMarkup;
import ru.telegram.model.sendMessage.SendMessageRequest;
import ru.telegram.repository.BankRepository;
import ru.telegram.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

import static java.util.Optional.ofNullable;

@Service("messageProcessorAddress")
@Slf4j
@AllArgsConstructor
public class MessageProcessorAddress implements MessageProcessor {
    private final RateServiceClientImpl rateServiceClientImpl;
    private final BankRepository bankRepository;
    private final UserRepository userRepository;
    private final ApplicationContext applicationContext;

    @Override
    public SendMessageRequest process(GetUpdatesResponse.Message message) {
        log.info("process: {}", "messageProcessorAddress");
        var msgText = message.getText() != null ? message.getText() : message.getReplyToMessage().getText();
        var userData = userRepository.findByUserId(message.getFrom().getId()).orElse(null);
        if (userData == null) {
            return selectProcess(Commands.CODE_BANK, message);
        }
        var codeBank = userData.getSelectedBank();
        var location = message.getLocation();
        var request = new BankAddressRequest();
        var answer = "";
        StringBuilder stringBuilder = new StringBuilder();
        if (msgText.isEmpty()) {
            return selectProcess(Commands.SEARCH_OFFICE, message);
        }
        request.setCodeBank(codeBank).setNameBank(bankRepository.findNameBankByCode(codeBank));
        ofNullable(message.getLocation()).ifPresent(x -> request.setLatitude(location.getLatitude()).setLongitude(location.getLongitude()));
        request.setAddressString(msgText);
        var response = rateServiceClientImpl.getBankAddressesWithCurrency(request);
        if (response.getError() != null) {
            var error = response.getError().getText();
            var errorFull = ofNullable(response.getError().getDescription()).orElse("");
            log.info("Произошла ошибка при попытки получения адресов отделений:\n{}\n{}", error, errorFull);
            answer = error;
        } else {
            var getAddresses = response.getData().getBankServiceAddressesResponse();
            stringBuilder.append("Выбранный банк: ");
            stringBuilder.append(getAddresses.getNameBank()).append(".\n").append("\n");
            stringBuilder.append("Ближайшие отделения к вашему адресу:").append("\n").append("\n");

            getAddresses.getAddresses().forEach(x -> {
                if (x.getAddressString() != null) {
                    stringBuilder.append(x.getAddressString()).append("\n");
                }
            });
            answer = stringBuilder.toString();
        }


        var result = new SendMessageRequest();
        var keyboardList = new ArrayList<List<Keyboard>>();
        var keyboardSection1 = new ArrayList<Keyboard>();
        keyboardSection1.add(new Keyboard().setText(Commands.ANOTHER_CURRENCY.getMessage()));
        keyboardSection1.add(new Keyboard().setText(Commands.ANOTHER_ADDRESS.getMessage()));
        keyboardSection1.add(new Keyboard().setText(Commands.ANOTHER_BANK.getMessage()));
        keyboardList.add(keyboardSection1);
        result.setText(answer);
        result.setReplyMarkup(new ReplyMarkup()
                .setKeyboard(keyboardList)
                .setResizeKeyboard(true)
                .setOneTimeKeyboard(false));
        result.setChatId(message.getChat().getId());
        return result;
    }

    private SendMessageRequest selectProcess(Commands command, GetUpdatesResponse.Message message) {
        var messageProcessor = applicationContext.getBean(command.getProcessor(), MessageProcessor.class);
        return messageProcessor.process(message);
    }
}
