package pl.dariuszgilewicz.util;

import lombok.experimental.UtilityClass;
import pl.dariuszgilewicz.infrastructure.database.entity.RestaurantOwnerEntity;
import pl.dariuszgilewicz.infrastructure.model.RestaurantOwner;

import static pl.dariuszgilewicz.util.RestaurantFixtures.*;

@UtilityClass
public class RestaurantOwnerFixtures {

    public static RestaurantOwner someRestaurantOwnerModel1() {
        return RestaurantOwner.builder()
                .name("Zbyszek")
                .surname("Pracowity")
                .pesel("91014452879")
                .restaurants(someListOfMappedRestaurants1())
                .build();
    }

    public static RestaurantOwner someRestaurantOwnerModel2() {
        return RestaurantOwner.builder()
                .name("Maciek")
                .surname("Pracowity")
                .pesel("97051242360")
                .restaurants(someListOfMappedRestaurants1())
                .build();
    }

    public static RestaurantOwner someRestaurantOwnerModel3(){
        return RestaurantOwner.builder()
                .name("Dawid")
                .surname("Goliat")
                .pesel("87142352813")
                .restaurants(someListOfMappedRestaurants2())
                .build();
    }

//    public static RestaurantOwner someRestaurantOwnerModel4(){
//        return RestaurantOwner.builder()
//                .name("Tomek")
//                .surname("Gapiński")
//                .pesel("92100712943")
//                .build();
//    }

    public static RestaurantOwnerEntity someRestaurantOwnerEntity1() {
        return RestaurantOwnerEntity.builder()
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
//                .restaurants(someListOfRestaurantEntities1())
                .build();
    }

//    public static RestaurantOwnerEntity someRestaurantOwnerEntity4() {
//        return RestaurantOwnerEntity.builder()
//                .name("Tomek")
//                .surname("Gapiński")
//                .pesel("92100712943")
//                .build();
//    }
}
