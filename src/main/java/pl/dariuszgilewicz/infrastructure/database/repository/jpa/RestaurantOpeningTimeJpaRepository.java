package pl.dariuszgilewicz.infrastructure.database.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.dariuszgilewicz.infrastructure.database.entity.RestaurantOpeningTimeEntity;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Optional;

@Repository
public interface RestaurantOpeningTimeJpaRepository extends JpaRepository<RestaurantOpeningTimeEntity, Long> {
//    Optional<RestaurantOpeningTimeEntity> findByOpeningHourAndCloseHourAndDayOfWeek(LocalTime openingHour, LocalTime closeHour, DayOfWeek dayOfWeek);
}
