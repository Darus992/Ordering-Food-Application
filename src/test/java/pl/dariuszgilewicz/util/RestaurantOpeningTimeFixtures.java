package pl.dariuszgilewicz.util;

import lombok.experimental.UtilityClass;
import pl.dariuszgilewicz.infrastructure.database.entity.RestaurantOpeningTimeEntity;
import pl.dariuszgilewicz.infrastructure.model.RestaurantOpeningTime;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class RestaurantOpeningTimeFixtures {

    public static RestaurantOpeningTime someRestaurantOpeningTimeModel1() {
        return RestaurantOpeningTime.builder()
                .openingHour("15:00")
                .closeHour("23:00")
                .dayOfWeekFrom(DayOfWeek.MONDAY)
                .dayOfWeekTill(DayOfWeek.SATURDAY)
                .build();
    }

    public static RestaurantOpeningTime someRestaurantOpeningTimeModel2() {
        return RestaurantOpeningTime.builder()
                .openingHour("10:00")
                .closeHour("18:00")
                .dayOfWeekFrom(DayOfWeek.MONDAY)
                .dayOfWeekTill(DayOfWeek.FRIDAY)
                .build();
    }

    public static RestaurantOpeningTime someRestaurantOpeningTimeModel3() {
        return RestaurantOpeningTime.builder()
                .openingHour("10:00:00")
                .closeHour("18:00:00")
                .dayOfWeekFrom(DayOfWeek.FRIDAY)
                .dayOfWeekTill(DayOfWeek.TUESDAY)
                .build();
    }

    public static RestaurantOpeningTimeEntity someRestaurantOpeningTimeEntity1() {
        return RestaurantOpeningTimeEntity.builder()
                .openingHour(LocalTime.parse("15:00", DateTimeFormatter.ISO_LOCAL_TIME))
                .closeHour(LocalTime.parse("23:00", DateTimeFormatter.ISO_LOCAL_TIME))
                .dayOfWeekFrom(DayOfWeek.MONDAY)
                .dayOfWeekTill(DayOfWeek.SATURDAY)
                .build();
    }

    public static RestaurantOpeningTimeEntity someRestaurantOpeningTimeEntity2() {
        return RestaurantOpeningTimeEntity.builder()
                .openingHour(LocalTime.parse("10:00", DateTimeFormatter.ISO_LOCAL_TIME))
                .closeHour(LocalTime.parse("18:00", DateTimeFormatter.ISO_LOCAL_TIME))
                .dayOfWeekFrom(DayOfWeek.MONDAY)
                .dayOfWeekTill(DayOfWeek.FRIDAY)
                .build();
    }
}
