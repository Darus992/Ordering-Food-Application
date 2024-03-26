package pl.dariuszgilewicz.util;

import lombok.experimental.UtilityClass;
import pl.dariuszgilewicz.infrastructure.database.entity.AddressEntity;
import pl.dariuszgilewicz.infrastructure.database.entity.RestaurantEntity;
import pl.dariuszgilewicz.infrastructure.database.entity.RestaurantOpeningTimeEntity;
import pl.dariuszgilewicz.infrastructure.database.entity.RestaurantOwnerEntity;
import pl.dariuszgilewicz.infrastructure.model.Address;
import pl.dariuszgilewicz.infrastructure.model.Restaurant;
import pl.dariuszgilewicz.infrastructure.model.RestaurantOpeningTime;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@UtilityClass
public class RestaurantFixtures {

    public static RestaurantEntity someRestaurantEntity1() {
        return RestaurantEntity.builder()
                .restaurantName("Na Wypasie")
                .phone("4577816539")
                .email("na_wypasie@restaurant.pl")
                .restaurantAddress(AddressEntity.builder()
                        .city("Białystok")
                        .district("Antoniuk")
                        .postalCode("12-221")
                        .address("Antoniukowska 100")
                        .build())
                .restaurantOwner(RestaurantOwnerEntity.builder()
                        .name("Zbyszek")
                        .surname("Pracowity")
                        .pesel("91014452879")
                        .build())
                .restaurantOpeningTime(RestaurantOpeningTimeEntity.builder()
                        .openingHour(LocalTime.parse("15:00", DateTimeFormatter.ISO_LOCAL_TIME))
                        .closeHour(LocalTime.parse("23:00", DateTimeFormatter.ISO_LOCAL_TIME))
                        .dayOfWeekFrom(DayOfWeek.MONDAY)
                        .dayOfWeekTill(DayOfWeek.SATURDAY)
                        .build())
                .build();
    }
    public static RestaurantEntity someRestaurantEntity2() {
        return RestaurantEntity.builder()
                .restaurantName("Zapiecek")
                .phone("4417811209")
                .email("zapiecek@restaurant.pl")
                .restaurantAddress(AddressEntity.builder()
                        .city("Warszawa")
                        .district("Zacisze")
                        .postalCode("11-253")
                        .address("Lisia 2")
                        .build())
                .restaurantOwner(RestaurantOwnerEntity.builder()
                        .name("Tomek")
                        .surname("Gapiński")
                        .pesel("92100712943")
                        .build())
                .restaurantOpeningTime(RestaurantOpeningTimeEntity.builder()
                        .openingHour(LocalTime.parse("15:00", DateTimeFormatter.ISO_LOCAL_TIME))
                        .closeHour(LocalTime.parse("23:00", DateTimeFormatter.ISO_LOCAL_TIME))
                        .dayOfWeekFrom(DayOfWeek.MONDAY)
                        .dayOfWeekTill(DayOfWeek.SATURDAY)
                        .build())
                .build();
    }
    public static RestaurantEntity someRestaurantEntity3(){
        return RestaurantEntity.builder()
                .restaurantName("Karczma")
                .phone("782906225")
                .email("karczma@restauracja.pl")
                .restaurantAddress(AddressEntity.builder()
                        .city("Kraków")
                        .district("Olsza")
                        .postalCode("22-123")
                        .address("Orszakowa 78")
                        .build())
                .restaurantOwner(RestaurantOwnerEntity.builder()
                        .name("Zbyszek")
                        .surname("Pracowity")
                        .pesel("91014452879")
                        .build())
                .restaurantOpeningTime(RestaurantOpeningTimeEntity.builder()
                        .openingHour(LocalTime.parse("10:00", DateTimeFormatter.ISO_LOCAL_TIME))
                        .closeHour(LocalTime.parse("18:00", DateTimeFormatter.ISO_LOCAL_TIME))
                        .dayOfWeekFrom(DayOfWeek.MONDAY)
                        .dayOfWeekTill(DayOfWeek.FRIDAY)
                        .build())
                .build();
    }
    public static RestaurantEntity someRestaurantEntity4(){
        return RestaurantEntity.builder()
                .restaurantName("Na Wypasie")
                .phone("4577816539")
                .email("na_wypasie@restaurant.pl")
                .foodMenu(FoodMenuFixtures.someFoodMenuEntity2())
                .restaurantAddress(AddressEntity.builder()
                        .city("Białystok")
                        .district("Antoniuk")
                        .postalCode("12-221")
                        .address("Antoniukowska 100")
                        .build())
                .restaurantOwner(RestaurantOwnerEntity.builder()
                        .name("Zbyszek")
                        .surname("Pracowity")
                        .pesel("91014452879")
                        .build())
                .restaurantOpeningTime(RestaurantOpeningTimeEntity.builder()
                        .openingHour(LocalTime.parse("15:00", DateTimeFormatter.ISO_LOCAL_TIME))
                        .closeHour(LocalTime.parse("23:00", DateTimeFormatter.ISO_LOCAL_TIME))
                        .dayOfWeekFrom(DayOfWeek.MONDAY)
                        .dayOfWeekTill(DayOfWeek.SATURDAY)
                        .build())
                .build();
    }
    public static RestaurantEntity someRestaurantEntity5() {
        return RestaurantEntity.builder()
                .restaurantName("Zapiecek")
                .phone("4417811209")
                .email("zapiecek@restaurant.pl")
                .foodMenu(FoodMenuFixtures.someFoodMenuEntity3())
                .restaurantAddress(AddressEntity.builder()
                        .city("Warszawa")
                        .district("Zacisze")
                        .postalCode("11-253")
                        .address("Lisia 2")
                        .build())
                .restaurantOwner(RestaurantOwnerEntity.builder()
                        .name("Tomek")
                        .surname("Gapiński")
                        .pesel("92100712943")
                        .build())
                .restaurantOpeningTime(RestaurantOpeningTimeEntity.builder()
                        .openingHour(LocalTime.parse("15:00", DateTimeFormatter.ISO_LOCAL_TIME))
                        .closeHour(LocalTime.parse("23:00", DateTimeFormatter.ISO_LOCAL_TIME))
                        .dayOfWeekFrom(DayOfWeek.MONDAY)
                        .dayOfWeekTill(DayOfWeek.SATURDAY)
                        .build())
                .build();
    }
    public static Restaurant someRestaurantModel1() {
        return Restaurant.builder()
                .restaurantName("Na Wypasie")
                .restaurantPhone("4577816539")
                .restaurantEmail("na_wypasie@restaurant.pl")
//                .foodMenu(FoodMenu.builder()
//                        .foodMenuName()
//                        .foods()
//                        .build())
                .restaurantAddress(Address.builder()
                        .city("Białystok")
                        .district("Antoniuk")
                        .postalCode("12-221")
                        .addressStreet("Antoniukowska 100")
                        .build())
                .restaurantOpeningTime(RestaurantOpeningTime.builder()
                        .openingHour("15:00")
                        .closeHour("23:00")
                        .dayOfWeekFrom(DayOfWeek.MONDAY)
                        .dayOfWeekTill(DayOfWeek.SATURDAY)
                        .build())
                .build();
    }

    public static Restaurant someRestaurantModel2() {
        return Restaurant.builder()
                .restaurantName("Zapiecek")
                .restaurantPhone("4417811209")
                .restaurantEmail("zapiecek@restaurant.pl")
//                .foodMenu(FoodMenu.builder()
//                        .foodMenuName()
//                        .foods()
//                        .build())
                .restaurantAddress(Address.builder()
                        .city("Warszawa")
                        .district("Zacisze")
                        .postalCode("11-253")
                        .addressStreet("Lisia 2")
                        .build())
                .restaurantOpeningTime(RestaurantOpeningTime.builder()
                        .openingHour("15:00")
                        .closeHour("23:00")
                        .dayOfWeekFrom(DayOfWeek.MONDAY)
                        .dayOfWeekTill(DayOfWeek.SATURDAY)
                        .build())
                .build();
    }
    public static List<RestaurantEntity> someListOfRestaurantEntities1(){
        return List.of(someRestaurantEntity1(), someRestaurantEntity2());
    }
    public static List<Restaurant> someListOfMappedRestaurants1(){
        return List.of(someRestaurantModel1(), someRestaurantModel2());
    }
}
