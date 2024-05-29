package pl.dariuszgilewicz.util;

import lombok.experimental.UtilityClass;
import pl.dariuszgilewicz.infrastructure.database.entity.OrdersEntity;
import pl.dariuszgilewicz.infrastructure.database.entity.RestaurantEntity;
import pl.dariuszgilewicz.infrastructure.model.Restaurant;

import java.util.List;

import static pl.dariuszgilewicz.util.AddressFixtures.*;
import static pl.dariuszgilewicz.util.FoodMenuFixtures.*;
import static pl.dariuszgilewicz.util.OrdersFixtures.someCustomerOrderNumbers1;
import static pl.dariuszgilewicz.util.OrdersFixtures.someOrdersEntityList3;
import static pl.dariuszgilewicz.util.RestaurantOpeningTimeFixtures.*;
import static pl.dariuszgilewicz.util.RestaurantOwnerFixtures.*;

@UtilityClass
public class RestaurantFixtures {

    public static RestaurantEntity someRestaurantEntity1() {
        return RestaurantEntity.builder()
                .restaurantName("Na Wypasie")
                .phone("4577816539")
                .email("na_wypasie@restaurant.pl")
                .restaurantAddress(someAddressEntity1())
                .restaurantOwner(someRestaurantOwnerEntity1())
                .restaurantOpeningTime(someRestaurantOpeningTimeEntity1())
                .build();
    }

    public static RestaurantEntity someRestaurantEntity2() {
        return RestaurantEntity.builder()
                .restaurantName("Zapiecek")
                .phone("4417811209")
                .email("zapiecek@restaurant.pl")
                .restaurantAddress(someAddressEntity4())
                .restaurantOpeningTime(someRestaurantOpeningTimeEntity2())
                .build();
    }

    public static RestaurantEntity someRestaurantEntity3(){
        byte[] content = {84, 101, 115, 116, 32, 99, 111, 110, 116, 101, 110, 116};

        return RestaurantEntity.builder()
                .restaurantImageCard(content)
                .restaurantImageHeader(content)
                .restaurantName("Karczma")
                .phone("782906225")
                .email("karczma@restauracja.pl")
                .foodMenu(someFoodMenuEntity1())
                .restaurantAddress(someAddressEntity3())
                .restaurantOpeningTime(someRestaurantOpeningTimeEntity2())
                .restaurantOwner(someRestaurantOwnerEntity1())
//                .customerOrders(someOrdersEntityList3())
                .build();
    }

    public static RestaurantEntity someRestaurantEntity4(){
        return RestaurantEntity.builder()
                .restaurantName("Na Wypasie")
                .phone("4577816539")
                .email("na_wypasie@restaurant.pl")
                .foodMenu(someFoodMenuEntity2())
                .restaurantAddress(someAddressEntity1())
                .restaurantOwner(someRestaurantOwnerEntity1())
                .restaurantOpeningTime(someRestaurantOpeningTimeEntity1())
                .build();
    }

    public static RestaurantEntity someRestaurantEntity5() {
        return RestaurantEntity.builder()
                .restaurantImageCard(new byte[0])
                .restaurantImageHeader(new byte[0])
                .restaurantName("Zapiecek")
                .phone("4417811209")
                .email("zapiecek@restaurant.pl")
                .foodMenu(someFoodMenuEntity3())
                .restaurantAddress(someAddressEntity4())
                .restaurantOpeningTime(someRestaurantOpeningTimeEntity1())
//                .customerOrders(List.of())
                .build();
    }

    public static Restaurant someRestaurantModel1() {
        return Restaurant.builder()
                .restaurantName("Na Wypasie")
                .restaurantPhone("4577816539")
                .restaurantEmail("na_wypasie@restaurant.pl")
                .foodMenu(someFoodMenuModel1())
                .restaurantAddress(someAddressModel1())
                .restaurantOpeningTime(someRestaurantOpeningTimeModel1())
                .build();
    }

    public static Restaurant someRestaurantModel2() {
        return Restaurant.builder()
                .restaurantName("Zapiecek")
                .restaurantPhone("4417811209")
                .restaurantEmail("zapiecek@restaurant.pl")
                .foodMenu(someFoodMenuModel2())
                .restaurantAddress(someAddressModel4())
                .restaurantOpeningTime(someRestaurantOpeningTimeModel1())
                .build();
    }

    public static Restaurant someRestaurantModel3() {
        return Restaurant.builder()
                .restaurantName("Karczma")
                .restaurantPhone("782906225")
                .restaurantEmail("karczma@restauracja.pl")
                .foodMenu(someFoodMenuModel1())
                .restaurantAddress(someAddressModel3())
                .restaurantOpeningTime(someRestaurantOpeningTimeModel2())
                .customerOrdersNumbers(someCustomerOrderNumbers1())
                .build();
    }

    public static Restaurant someRestaurantModel5() {
        return Restaurant.builder()
                .restaurantImageCard("")
                .restaurantImageHeader("")
                .restaurantName("Zapiecek")
                .restaurantPhone("4417811209")
                .restaurantEmail("zapiecek@restaurant.pl")
                .foodMenu(someFoodMenuModel3())
                .restaurantAddress(someAddressModel4())
                .restaurantOpeningTime(someRestaurantOpeningTimeModel1())
                .customerOrdersNumbers(List.of())
                .build();
    }

    public static List<RestaurantEntity> someListOfRestaurantEntities1(){
        return List.of(someRestaurantEntity1(), someRestaurantEntity2());
    }
    public static List<RestaurantEntity> someListOfRestaurantEntities3(){
        return List.of(someRestaurantEntity5());
    }
    public static List<Restaurant> someListOfMappedRestaurants1(){
        return List.of(someRestaurantModel1(), someRestaurantModel2());
    }
    public static List<Restaurant> someListOfMappedRestaurants2(){
        return List.of(someRestaurantModel2(), someRestaurantModel3());
    }
    public static List<Restaurant> someListOfMappedRestaurants3(){
        return List.of(someRestaurantModel5());
    }
}
