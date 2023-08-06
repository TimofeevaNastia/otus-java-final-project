package ru.telegram.model.db;

import jakarta.annotation.Nonnull;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;

@Table("bank")
@Getter
public class Bank {
    @Id
    private Long id;

    @Nonnull
    private String name;

    @Nonnull
    private String code;


    public Bank(String name, String code) {
        this(null, name, code);
    }

    @PersistenceCreator
    public Bank(Long id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }
}
