package ru.rate.model.db;

import jakarta.annotation.Nonnull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("currency")
@Data
public class Currency {
    @Id
    private Long id;

    @Nonnull
    private String name;

    @Nonnull
    private int codeDigit;
    private String codeName;

    public Currency() {
    }

    public Currency(String name, int  codeDigit, String codeName) {
        this.name = name;
        this.codeName = codeName;
        this.codeDigit = codeDigit;
    }

    public Currency(Long id, String name, int  codeDigit, String codeName) {
        this.id = id;
        this.name = name;
        this.codeName = codeName;
        this.codeDigit = codeDigit;
    }

}
