package ru.telegram.repository;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.telegram.model.db.UserData;

import java.math.BigDecimal;
import java.util.Optional;


public interface UserRepository extends CrudRepository<UserData, Long> {

    @Query("select * from users_data where user_id = :user_id")
    Optional<UserData> findByUserId(@Param("user_id") long userId);

    @Modifying
    @Query("update users_data set scenario_id = :scenario_id where id = :id")
    void updateUserScenarioId(@Param("id") long id, @Param("scenario_id") long scenarioId);

    @Modifying
    @Query("update users_data set rate = :rate where id = :id")
    void updateUserRate(@Param("id") long id, @Param("rate") BigDecimal rate);

}
