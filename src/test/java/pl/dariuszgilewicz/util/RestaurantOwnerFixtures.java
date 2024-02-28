package pl.dariuszgilewicz.util;

import lombok.experimental.UtilityClass;
import pl.dariuszgilewicz.infrastructure.database.entity.RestaurantEntity;
import pl.dariuszgilewicz.infrastructure.database.entity.RestaurantOwnerEntity;
import pl.dariuszgilewicz.infrastructure.model.RestaurantOwner;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class RestaurantOwnerFixtures {

    public static RestaurantOwnerEntity someRestaurantOwnerEntity1() {
        return RestaurantOwnerEntity.builder()
                .name("Zbyszek")
                .surname("Pracowity")
                .pesel("91014452879")
                .build();
    }

    public static RestaurantOwner someRestaurantOwner1() {
        return RestaurantOwner.builder()
                .name("Zbyszek")
                .surname("Pracowity")
                .pesel("91014452879")
                .build();
    }

    public static RestaurantOwnerEntity someRestaurantOwnerEntity2() {
        return RestaurantOwnerEntity.builder()
                .name("Maciek")
                .surname("Pracowity")
                .pesel("97051242360")
                .restaurants(RestaurantFixtures.someListOfRestaurantEntities1())
                .build();
    }

    public static RestaurantOwner someRestaurantOwner2() {
        return RestaurantOwner.builder()
                .name("Maciek")
                .surname("Pracowity")
                .pesel("97051242360")
                .restaurants(RestaurantFixtures.someListOfMappedRestaurants1())
                .build();
    }
}
