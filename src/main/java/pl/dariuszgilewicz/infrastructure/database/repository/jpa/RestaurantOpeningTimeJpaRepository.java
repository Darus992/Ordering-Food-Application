package pl.dariuszgilewicz.infrastructure.database.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.dariuszgilewicz.infrastructure.database.entity.RestaurantOpeningTimeEntity;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Optional;

@Repository
public interface RestaurantOpeningTimeJpaRepository extends JpaRepository<RestaurantOpeningTimeEntity, Integer> {

    @Query("""
            SELECT e FROM RestaurantOpeningTimeEntity e WHERE e.openingHour = :openingHour
                        AND e.closeHour = :closeHour
                        AND e.dayOfWeekFrom = :dayOfWeekFrom
                        AND e.dayOfWeekTill = :dayOfWeekTill
            """)
    Optional<RestaurantOpeningTimeEntity> findEntityByTimeAndDay(
            @Param("openingHour") LocalTime openingHour,
            @Param("closeHour") LocalTime closeHour,
            @Param("dayOfWeekFrom") DayOfWeek dayOfWeekFrom,
            @Param("dayOfWeekTill") DayOfWeek dayOfWeekTill
    );
}
