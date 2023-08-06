package ru.telegram.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.telegram.model.db.Bank;

import java.util.List;


public interface BankRepository extends CrudRepository<Bank, Long> {

    @Override
    List<Bank> findAll();

    @Query("select name from bank where code = :code")
    String findNameBankByCode(@Param("code") String code);

}
