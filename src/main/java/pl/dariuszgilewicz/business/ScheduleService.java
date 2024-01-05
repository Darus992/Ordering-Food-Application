package pl.dariuszgilewicz.business;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dariuszgilewicz.infrastructure.database.entity.RestaurantEntity;
import pl.dariuszgilewicz.infrastructure.database.entity.ScheduleEntity;
import pl.dariuszgilewicz.infrastructure.database.repository.RestaurantRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.ScheduleRepository;
import pl.dariuszgilewicz.infrastructure.model.Schedule;

@Service
@AllArgsConstructor
public class ScheduleService {

    private ScheduleRepository scheduleRepository;
    private RestaurantRepository restaurantRepository;

    @Transactional
    public void createScheduleAndAddToRestaurant(Schedule schedule, String restaurantEmail) {
        ScheduleEntity scheduleEntity = scheduleRepository.createSchedule(schedule);
        RestaurantEntity restaurantEntity = restaurantRepository.findRestaurantByEmail(restaurantEmail);
        restaurantEntity.setSchedule(scheduleEntity);
        restaurantRepository.save(restaurantEntity);
    }
}
