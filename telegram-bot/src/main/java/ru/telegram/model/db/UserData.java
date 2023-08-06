package ru.telegram.model.db;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Table("users_data")
@Getter
@Setter
public class UserData {
    @Id
    private Long id;

    private Long userId;
    private Long chatId;
    private String selectedBank;
    private String selectedCurrency;
    private BigDecimal rate;
    private Integer scenarioId;

    public UserData(Long userId, Long chatId, String selectedBank, String selectedCurrency, BigDecimal rate, int scenarioId) {
        this(null, userId, chatId, selectedBank, selectedCurrency, rate, scenarioId);
    }

    @PersistenceCreator
    public UserData(Long id, Long userId, Long chatId, String selectedBank, String selectedCurrency, BigDecimal rate, int scenarioId) {
        this.id = id;
        this.userId = userId;
        this.chatId = chatId;
        this.selectedBank = selectedBank;
        this.selectedCurrency = selectedCurrency;
        this.rate = rate;
        this.scenarioId = scenarioId;
    }
}
