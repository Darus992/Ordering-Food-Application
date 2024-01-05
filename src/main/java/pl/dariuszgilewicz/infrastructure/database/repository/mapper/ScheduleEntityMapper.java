package pl.dariuszgilewicz.infrastructure.database.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pl.dariuszgilewicz.infrastructure.database.entity.RestaurantOpeningTimeEntity;
import pl.dariuszgilewicz.infrastructure.database.entity.ScheduleEntity;
import pl.dariuszgilewicz.infrastructure.model.RestaurantOpeningTime;
import pl.dariuszgilewicz.infrastructure.model.Schedule;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ScheduleEntityMapper {

    default ScheduleEntity mapToEntity(Schedule schedule){
        return ScheduleEntity.builder()
                .restaurantOpeningTimes(mapRestaurantOpeningTimeListToEntity(schedule.getRestaurantOpeningTimes()))
                .build();
    }

    default List<RestaurantOpeningTimeEntity> mapRestaurantOpeningTimeListToEntity(List<RestaurantOpeningTime> openingTimeList){
        List<RestaurantOpeningTimeEntity> entityList = new ArrayList<>();

        for (RestaurantOpeningTime restaurantOpeningTime : openingTimeList) {
            RestaurantOpeningTimeEntity entity = mapRestaurantOpeningTimeToEntity(restaurantOpeningTime);
            entityList.add(entity);
        }
        return entityList;
    }

    default RestaurantOpeningTimeEntity mapRestaurantOpeningTimeToEntity(RestaurantOpeningTime restaurantOpeningTime){
        return RestaurantOpeningTimeEntity.builder()
                .openingHour(LocalTime.parse(restaurantOpeningTime.getOpeningHour()))
                .closeHour(LocalTime.parse(restaurantOpeningTime.getCloseHour()))
                .dayOfWeek(restaurantOpeningTime.getDayOfWeek())
                .build();
    }
}
