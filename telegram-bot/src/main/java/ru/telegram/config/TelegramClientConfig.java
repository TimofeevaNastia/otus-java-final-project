package ru.telegram.config;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
public class TelegramClientConfig {
    String url;
    String token;
}
