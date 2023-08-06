package ru.rate.model.db;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;

@Table("addresses")
@Getter
@Setter
public class Addresses {
    @Id
    private Long id;

    private String city;

    private String street;

    private String home;

    private String building;



    public Addresses(String city, String street, String home, String building) {
        this(null, street, city, home, building);
    }

    @PersistenceCreator
    public Addresses(Long id,  String city, String street, String home, String building) {
        this.id = id;
        this.city = city;
        this.street = street;
        this.home = home;
        this.building = building;
    }

}
