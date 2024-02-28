package pl.dariuszgilewicz.util;

import lombok.experimental.UtilityClass;
import pl.dariuszgilewicz.infrastructure.request_form.RestaurantRequestForm;

import java.time.DayOfWeek;

@UtilityClass
public class RestaurantRequestFormFixtures {

    public static RestaurantRequestForm someRestaurantRequestForm1(){
        return RestaurantRequestForm.builder()
                .restaurantName("Karczma")
                .restaurantPhone("782906225")
                .restaurantEmail("karczma@restauracja.pl")
                .restaurantAddressCity("Kraków")
                .restaurantAddressDistrict("Olsza")
                .restaurantAddressPostalCode("22-123")
                .restaurantAddressStreet("Orszakowa 78")
                .openingHour("10:00")
                .closeHour("18:00")
                .dayOfWeekFrom(DayOfWeek.MONDAY)
                .dayOfWeekTill(DayOfWeek.FRIDAY)
                .build();
    }
}
