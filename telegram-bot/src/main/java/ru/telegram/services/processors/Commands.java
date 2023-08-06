package ru.telegram.services.processors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Optional.ofNullable;

@Getter
@RequiredArgsConstructor
public enum Commands {
    START("start", "messageProcessorStart", 0),
    ANOTHER_CURRENCY("Выбрать другую валюту", "messageProcessorStart", 0),
    SEARCH_OFFICE("Найти ближайшее отделение", "messageProcessorSearchOffice", 2),
    SEND_LOCATION("Отправить местоположение", "messageProcessorSearchOffice", 2),
    CALCULATE("Рассчитать сумму", "messageProcessorCalculate", 4),
    CODE_BANK("Выбрать банк", "messageProcessorBank", 5),
    GET_RATE("^(Купить|Продать) ([A-Z]{3})$", "messageProcessorRate", 6),
    ENTER_ADDRESS("Ввод адреса", "messageProcessorAddress", 2),
    ANOTHER_ADDRESS("Ввести другой адрес", "messageProcessorSearchOffice", 2),
    ANOTHER_BANK("Выбрать другой банк", "messageProcessorRate", 6),
    ENTER_SUM("Введите сумму", "messageProcessorSum", 10),
    CALCULATE_SUM("Введена сумма", "messageProcessorCalculateSum", 11),
    CALCULATE_ANOTHER_SUM("Рассчитать другую сумму", "messageProcessorCalculate", 4),
    OTHER(null, null, 0);

    private final String message;
    private final String processor;
    private final int scenarioId;

    public static String getProcessor(String message) {
        return Arrays.stream(values()).filter(x -> {
            Pattern pattern = Pattern.compile(ofNullable(x.getMessage()).orElse(""));
            Matcher matcher = pattern.matcher(message);
            return matcher.find();
        }).findFirst().orElse(OTHER).getProcessor();
    }

    public static Commands getCommand(String message) {
        return Arrays.stream(values()).filter(x -> {
            Pattern pattern = Pattern.compile(ofNullable(x.getMessage()).orElse(""));
            Matcher matcher = pattern.matcher(message);
            return matcher.find();
        }).findFirst().orElse(OTHER);
    }

    public static int getScenario(String processor) {
        var command = Arrays.stream(values())
                .filter(x -> processor.equals(x.getProcessor()))
                .findFirst()
                .orElse(OTHER);
        return command.getScenarioId();
    }
}
