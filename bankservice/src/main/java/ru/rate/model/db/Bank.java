package ru.rate.model.db;

import jakarta.annotation.Nonnull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Set;

@Table("bank")
@Getter
@Setter
@Accessors(chain = true)
public class Bank {
    @Id
    private Long id;

    @Nonnull
    private String name;

    @Nonnull
    private String code;

    @MappedCollection(idColumn = "bank_id")
    private Set<Addresses> addresses;


    public Bank(String name, String code, Set<Addresses> addresses) {
        this(null, name, code, addresses);
    }

    @PersistenceCreator
    public Bank(Long id, String name, String code, Set<Addresses> addresses) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.addresses = addresses;
    }
}
