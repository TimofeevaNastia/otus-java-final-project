package ru.rate.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.rate.model.db.Addresses;
import ru.rate.model.db.Bank;

import java.util.List;


public interface BankRepository extends CrudRepository<Bank, Long> {

    @Query("""
            select a.* from bank b
            join addresses a on b.id = a.bank_id and b.code = :code
            where a.city like :city
            """)
    List<Addresses> findAddressesOfBankByCity(@Param("code") String code, @Param("city") String city);

}
