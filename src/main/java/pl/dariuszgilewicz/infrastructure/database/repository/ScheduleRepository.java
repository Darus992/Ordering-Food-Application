package pl.dariuszgilewicz.infrastructure.database.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.dariuszgilewicz.infrastructure.database.entity.RestaurantOpeningTimeEntity;
import pl.dariuszgilewicz.infrastructure.database.entity.ScheduleEntity;
import pl.dariuszgilewicz.infrastructure.database.repository.jpa.RestaurantOpeningTimeJpaRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.jpa.ScheduleJpaRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.mapper.ScheduleEntityMapper;
import pl.dariuszgilewicz.infrastructure.model.Schedule;

import java.util.List;

@Repository
@AllArgsConstructor
public class ScheduleRepository {

    private ScheduleJpaRepository scheduleJpaRepository;
    private ScheduleEntityMapper scheduleEntityMapper;

    private RestaurantOpeningTimeJpaRepository restaurantOpeningTimeJpaRepository;

    public ScheduleEntity createSchedule(Schedule schedule) {
        ScheduleEntity toSave = scheduleEntityMapper.mapToEntity(schedule);
        List<RestaurantOpeningTimeEntity> restaurantOpeningTimes = toSave.getRestaurantOpeningTimes();
        restaurantOpeningTimeJpaRepository.saveAll(restaurantOpeningTimes);
        scheduleJpaRepository.save(toSave);
        return toSave;
    }
}
