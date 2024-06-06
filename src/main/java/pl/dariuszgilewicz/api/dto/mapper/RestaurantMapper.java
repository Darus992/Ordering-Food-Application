package pl.dariuszgilewicz.api.dto.mapper;

import org.springframework.stereotype.Component;
import pl.dariuszgilewicz.api.dto.RestaurantDTO;
import pl.dariuszgilewicz.infrastructure.database.entity.RestaurantEntity;
import pl.dariuszgilewicz.infrastructure.model.Address;
import pl.dariuszgilewicz.infrastructure.model.Restaurant;
import pl.dariuszgilewicz.infrastructure.model.RestaurantOpeningTime;

import java.time.DayOfWeek;
import java.util.List;

@Component
public class RestaurantMapper {

    public RestaurantDTO mapToDTO(Restaurant restaurant, String baseUrl) {
        return RestaurantDTO.builder()
                .restaurantImageCard(baseUrl + "/image/" + restaurant.getRestaurantEmail() + "?image=CARD")
                .restaurantImageHeader(baseUrl + "/image/" + restaurant.getRestaurantEmail() + "?image=HEADER")
                .restaurantName(restaurant.getRestaurantName())
                .restaurantPhone(restaurant.getRestaurantPhone())
                .restaurantEmail(restaurant.getRestaurantEmail())
                .restaurantCity(restaurant.getRestaurantAddress().getCity())
                .restaurantDistrict(restaurant.getRestaurantAddress().getDistrict())
                .restaurantPostalCode(restaurant.getRestaurantAddress().getPostalCode())
                .restaurantAddressStreet(restaurant.getRestaurantAddress().getAddressStreet())
                .openingHour(restaurant.getRestaurantOpeningTime().getOpeningHour())
                .closeHour(restaurant.getRestaurantOpeningTime().getCloseHour())
                .dayOfWeekFrom(restaurant.getRestaurantOpeningTime().getDayOfWeekFrom().toString())
                .dayOfWeekTill(restaurant.getRestaurantOpeningTime().getDayOfWeekTill().toString())
                .customerOrdersNumbers(restaurant.getCustomerOrdersNumbers())
                .build();
    }

    public List<RestaurantDTO> mapToDTOList(List<Restaurant> restaurants, String baseUrl) {
        return restaurants.stream()
                .map((Restaurant restaurant) -> mapToDTO(restaurant, baseUrl))
                .toList();
    }

    public Restaurant mapRestaurantDetailsFormToUpdateFromDTO(
            String restaurantName,
            String restaurantPhone,
            String restaurantEmailToUpdate,
            String restaurantAddressCity,
            String restaurantAddressDistrict,
            String restaurantAddressPostalCode,
            String restaurantAddressStreet,
            RestaurantEntity currentRestaurant
    ) {
        return Restaurant.builder()
                .restaurantName(restaurantName == null ? currentRestaurant.getRestaurantName() : restaurantName)
                .restaurantPhone(restaurantPhone == null ? currentRestaurant.getPhone() : restaurantPhone)
                .restaurantEmail(restaurantEmailToUpdate == null ? currentRestaurant.getEmail() : restaurantEmailToUpdate)
                .restaurantAddress(Address.builder()
                        .city(restaurantAddressCity == null ? currentRestaurant.getRestaurantAddress().getCity() : restaurantAddressCity)
                        .district(restaurantAddressDistrict == null ? currentRestaurant.getRestaurantAddress().getDistrict() : restaurantAddressDistrict)
                        .postalCode(restaurantAddressPostalCode == null ? currentRestaurant.getRestaurantAddress().getPostalCode() : restaurantAddressPostalCode)
                        .addressStreet(restaurantAddressStreet == null ? currentRestaurant.getRestaurantAddress().getAddress() : restaurantAddressStreet)
                        .build())
                .build();
    }

    public Restaurant mapScheduleDataToRestaurant(
            String openingHour,
            String closeHour,
            DayOfWeek dayOfWeekFrom,
            DayOfWeek dayOfWeekTill,
            RestaurantEntity currentRestaurant
    ) {
        return Restaurant.builder()
                .restaurantOpeningTime(RestaurantOpeningTime.builder()
                        .openingHour(openingHour == null ? currentRestaurant.getRestaurantOpeningTime().getOpeningHour().toString() : openingHour)
                        .closeHour(closeHour == null ? currentRestaurant.getRestaurantOpeningTime().getCloseHour().toString() : closeHour)
                        .dayOfWeekFrom(dayOfWeekFrom)
                        .dayOfWeekTill(dayOfWeekTill)
                        .build())
                .build();
    }
}
