package ru.telegram.services.processors;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import ru.telegram.model.db.UserData;
import ru.telegram.model.getUpdates.GetUpdatesResponse;
import ru.telegram.model.sendMessage.SendMessageRequest;
import ru.telegram.repository.BankRepository;
import ru.telegram.repository.UserRepository;

@Service("messageProcessor")
@Slf4j
@AllArgsConstructor
public class MessageProcessorGeneral implements MessageProcessor {
    private final ApplicationContext applicationContext;
    private final UserRepository userRepository;
    private final BankRepository bankRepository;

    @Override
    public SendMessageRequest process(GetUpdatesResponse.Message message) {
        log.info("Анализ сообщения. Выбор подходящего процесса");
        var msgText = message.getText() != null ? message.getText() : message.getReplyToMessage().getText();
        var processor = Commands.getProcessor(msgText);
        var user = userRepository.findByUserId(message.getFrom().getId()).orElse(null);

        if (processor != null) {
            var messageProcessor = applicationContext.getBean(processor, MessageProcessor.class);
            if (user != null) {
                userRepository.updateUserScenarioId(user.getId(), Commands.getScenario(processor));
            }
            return messageProcessor.process(message);
        }

        if (user != null) {
            var scenarioIdUser = user.getScenarioId();
            log.info("найден user в БД, scenarioIdUser: {}", scenarioIdUser);
            if (scenarioIdUser == Commands.SEARCH_OFFICE.getScenarioId()) {
                var messageProcessorAddress = applicationContext.getBean(Commands.ENTER_ADDRESS.getProcessor(), MessageProcessor.class);
                var request = messageProcessorAddress.process(message);
                userRepository.updateUserScenarioId(user.getId(), Commands.ENTER_ADDRESS.getScenarioId());
                return request;
            }

            if (scenarioIdUser == Commands.CALCULATE.getScenarioId()) {
                var messageProcessor = applicationContext.getBean(Commands.CALCULATE_SUM.getProcessor(), MessageProcessor.class);
                return messageProcessor.process(message);
            }

        }

        var selectedBank = bankRepository.findAll().stream().filter(x -> msgText.contains(x.getCode())).findFirst();
        var selectBank = selectedBank.isPresent();

        if (selectBank) {
            var userRepId = user == null ? null : user.getId();
            var savedCurrency = user == null ? null : user.getSelectedCurrency();
            var rate = user == null ? null : user.getRate();
            userRepository.save(new UserData(userRepId, message.getFrom().getId(),
                    message.getChat().getId(),
                    selectedBank.get().getCode(),
                    savedCurrency,
                    rate,
                    Commands.CODE_BANK.getScenarioId()));
            var messageProcessorBank = applicationContext.getBean(Commands.CODE_BANK.getProcessor(), MessageProcessor.class);
            return messageProcessorBank.process(message);
        }

        log.warn("Сообщение '{}' не распознано.", msgText);
        var result = new SendMessageRequest();
        result.setChatId(message.getChat().getId());
        result.setText("Выберите предложенное действие в панели снизу или начните заново /start");
        return result;
    }

}
