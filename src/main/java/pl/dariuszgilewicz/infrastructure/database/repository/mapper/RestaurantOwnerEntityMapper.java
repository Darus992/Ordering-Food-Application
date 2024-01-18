package pl.dariuszgilewicz.infrastructure.database.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pl.dariuszgilewicz.infrastructure.database.entity.*;
import pl.dariuszgilewicz.infrastructure.model.Restaurant;
import pl.dariuszgilewicz.infrastructure.model.RestaurantOwner;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RestaurantOwnerEntityMapper {

    default RestaurantOwnerEntity mapToEntity(RestaurantOwner restaurantOwner) {
        return RestaurantOwnerEntity.builder()
                .name(restaurantOwner.getName())
                .surname(restaurantOwner.getSurname())
                .pesel(restaurantOwner.getPesel())
                .restaurants(mapRestaurantsEntityFromList(restaurantOwner.getRestaurants()))
                .build();
    }

    default List<RestaurantEntity> mapRestaurantsEntityFromList(List<Restaurant> restaurants) {
        List<RestaurantEntity> entities = new ArrayList<>();
        for (Restaurant restaurant : restaurants) {
            RestaurantEntity entity = mapRestaurantToEntity(restaurant);
            entities.add(entity);
        }
        return entities;
    }

    default RestaurantEntity mapRestaurantToEntity(Restaurant restaurant) {
        return RestaurantEntity.builder()
                .restaurantName(restaurant.getRestaurantName())
                .phone(restaurant.getRestaurantPhone())
                .email(restaurant.getRestaurantEmail())
                .foodMenu(FoodMenuEntity.builder()
                        .menuName(restaurant.getFoodMenu().getFoodMenuName())
//                        .foods()
                        .build())
                .restaurantAddress(AddressEntity.builder()
                        .city(restaurant.getRestaurantAddress().getAddressCity())
                        .district(restaurant.getRestaurantAddress().getAddressDistrict())
                        .postalCode(restaurant.getRestaurantAddress().getAddressPostalCode())
                        .address(restaurant.getRestaurantAddress().getAddressAddressStreet())
                        .build()
                )
                .restaurantOpeningTime(RestaurantOpeningTimeEntity.builder()
                        .openingHour(LocalTime.parse(restaurant.getRestaurantOpeningTime().getOpeningHour()))
                        .closeHour(LocalTime.parse(restaurant.getRestaurantOpeningTime().getCloseHour()))
                        .dayOfWeekFrom(restaurant.getRestaurantOpeningTime().getDayOfWeekFrom())
                        .dayOfWeekTill(restaurant.getRestaurantOpeningTime().getDayOfWeekTill())
                        .build())
                .build();
    }
}
