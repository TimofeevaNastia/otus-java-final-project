package ru.rate.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "bank")
public class BankUrlConfig {

    List<BankListConfig> banks;

    public BankListConfig getBank(String code) {
        return banks.stream()
                .filter(bank -> bank.getName().equals(code))
                .findFirst()
                .orElseThrow(() -> new ConfigException("Для данного банка не настроена конфигурация"));
    }
}
