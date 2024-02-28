package pl.dariuszgilewicz.util;

import lombok.experimental.UtilityClass;
import pl.dariuszgilewicz.infrastructure.request_form.BusinessRequestForm;

import java.time.DayOfWeek;

@UtilityClass
public class BusinessRequestFormFixtures {

    public static BusinessRequestForm someBusinessRequestForm1() {
        return BusinessRequestForm.builder()
                .username("business_user")
                .userEmail("business@business_user.com")
                .userPassword("haslo")
                .ownerName("Zbyszek")
                .ownerSurname("Pracowity")
                .ownerPesel("91014452879")
                .restaurantName("Na Wypasie")
                .restaurantPhone("4577816539")
                .restaurantEmail("na_wypasie@restaurant.pl")
                .restaurantAddressCity("Białystok")
                .restaurantAddressDistrict("Antoniuk")
                .restaurantAddressPostalCode("12-221")
                .restaurantAddressStreet("Antoniukowska 100")
                .openingHour("15:00")
                .closeHour("23:00")
                .dayOfWeekFrom(DayOfWeek.MONDAY)
                .dayOfWeekTill(DayOfWeek.SATURDAY)
                .build();
    }

    public static BusinessRequestForm someBusinessRequestForm2() {
        return BusinessRequestForm.builder()
                .username("business_user")
                .userEmail("business@business_user.com")
                .userPassword("haslo")
                .ownerName("Maciek")
                .ownerSurname("Pracowity")
                .ownerPesel("97051242360")
                .restaurantName("Na Wypasie")
                .restaurantPhone("4577816539")
                .restaurantEmail("na_wypasie@restaurant.pl")
                .restaurantAddressCity("Białystok")
                .restaurantAddressDistrict("Antoniuk")
                .restaurantAddressPostalCode("12-221")
                .restaurantAddressStreet("Antoniukowska 100")
                .openingHour("15:00")
                .closeHour("23:00")
                .dayOfWeekFrom(DayOfWeek.MONDAY)
                .dayOfWeekTill(DayOfWeek.SATURDAY)
                .build();
    }
}
