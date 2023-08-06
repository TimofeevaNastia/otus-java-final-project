package ru.address.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "yandex")
public class YandexConfig {
    String url;
}
