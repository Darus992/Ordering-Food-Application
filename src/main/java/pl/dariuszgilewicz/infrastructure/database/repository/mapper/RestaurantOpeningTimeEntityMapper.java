package pl.dariuszgilewicz.infrastructure.database.repository.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.dariuszgilewicz.infrastructure.database.entity.RestaurantOpeningTimeEntity;
import pl.dariuszgilewicz.infrastructure.model.RestaurantOpeningTime;
import pl.dariuszgilewicz.infrastructure.request_form.BusinessRequestForm;
import pl.dariuszgilewicz.infrastructure.request_form.RestaurantRequestForm;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Component
@AllArgsConstructor
public class RestaurantOpeningTimeEntityMapper {

    public RestaurantOpeningTime mapFromEntity(RestaurantOpeningTimeEntity entity) {
        return RestaurantOpeningTime.builder()
                .openingHour(entity.getOpeningHour().toString())
                .closeHour(entity.getCloseHour().toString())
                .dayOfWeekFrom(entity.getDayOfWeekFrom())
                .dayOfWeekTill(entity.getDayOfWeekTill())
                .build();
    }

    public RestaurantOpeningTimeEntity mapToEntity(RestaurantOpeningTime restaurantOpeningTime) {
        return RestaurantOpeningTimeEntity.builder()
                .openingHour(LocalTime.parse(restaurantOpeningTime.getOpeningHour() + ":00", DateTimeFormatter.ISO_LOCAL_TIME))
                .closeHour(LocalTime.parse(restaurantOpeningTime.getCloseHour() + ":00", DateTimeFormatter.ISO_LOCAL_TIME))
                .dayOfWeekFrom(restaurantOpeningTime.getDayOfWeekFrom())
                .dayOfWeekTill(restaurantOpeningTime.getDayOfWeekTill())
                .build();
    }

    public RestaurantOpeningTimeEntity mapFromBusinessRequest(BusinessRequestForm requestForm) {
        return RestaurantOpeningTimeEntity.builder()
                .openingHour(LocalTime.parse(requestForm.getOpeningHour() + ":00", DateTimeFormatter.ISO_LOCAL_TIME))
                .closeHour(LocalTime.parse(requestForm.getCloseHour() + ":00", DateTimeFormatter.ISO_LOCAL_TIME))
                .dayOfWeekFrom(requestForm.getDayOfWeekFrom())
                .dayOfWeekTill(requestForm.getDayOfWeekTill())
                .build();
    }

    public RestaurantOpeningTimeEntity mapFromRestaurantRequest(RestaurantRequestForm requestForm) {
        return RestaurantOpeningTimeEntity.builder()
                .openingHour(LocalTime.parse(requestForm.getOpeningHour() + ":00", DateTimeFormatter.ISO_LOCAL_TIME))
                .closeHour(LocalTime.parse(requestForm.getCloseHour() + ":00", DateTimeFormatter.ISO_LOCAL_TIME))
                .dayOfWeekFrom(requestForm.getDayOfWeekFrom())
                .dayOfWeekTill(requestForm.getDayOfWeekTill())
                .build();
    }
}
