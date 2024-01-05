package pl.dariuszgilewicz.infrastructure.database.repository.mapper;

import org.mapstruct.*;
import pl.dariuszgilewicz.infrastructure.database.entity.RestaurantEntity;
import pl.dariuszgilewicz.infrastructure.database.entity.RestaurantOpeningTimeEntity;
import pl.dariuszgilewicz.infrastructure.model.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RestaurantEntityMapper {

//    @Mapping(source = "phone", target = "restaurantPhone")
//    @Mapping(source = "email", target = "restaurantEmail")
//    @Mapping(source = "restaurantAddress.city", target = "restaurantAddress.addressCity")
//    @Mapping(source = "restaurantAddress.district", target = "restaurantAddress.addressDistrict")
//    @Mapping(source = "schedule", target = "restaurantSchedule")
//    Restaurant mapFromEntity(RestaurantEntity entity);

    default Restaurant mapFromEntity(RestaurantEntity entity){
        return Restaurant.builder()
                .restaurantName(entity.getRestaurantName())
                .restaurantPhone(entity.getPhone())
                .restaurantEmail(entity.getEmail())
                .foodMenu(FoodMenu.builder()
                        .foodMenuName(entity.getFoodMenu().getMenuName())
                        .build())
                .restaurantAddress(Address.builder()
                        .addressCity(entity.getRestaurantAddress().getCity())
                        .addressDistrict(entity.getRestaurantAddress().getDistrict())
                        .addressPostalCode(entity.getRestaurantAddress().getPostalCode())
                        .addressAddressStreet(entity.getRestaurantAddress().getAddress())
                        .build()
                )
                .restaurantSchedule(Schedule.builder()
                        .restaurantOpeningTimes(mapFromEntityRestaurantOpeningTimeList(entity.getSchedule().getRestaurantOpeningTimes()))
                        .build())
                .build();
    }

    default List<Restaurant> mapAllFromEntity(List<RestaurantEntity> allEntity) {
        List<Restaurant> resultList = new ArrayList<>();
        for (RestaurantEntity restaurantEntity : allEntity) {
            Restaurant restaurant = mapFromEntity(restaurantEntity);
            resultList.add(restaurant);
        }
        return resultList;
    }

    @Mapping(source = "restaurantPhone", target = "phone")
    @Mapping(source = "restaurantEmail", target = "email")
    @Mapping(source = "foodMenu.foodMenuName", target = "foodMenu.menuName")
    @Mapping(source = "restaurantAddress.addressCity", target = "restaurantAddress.city")
    @Mapping(source = "restaurantAddress.addressDistrict", target = "restaurantAddress.district")
    @Mapping(source = "restaurantAddress.addressPostalCode", target = "restaurantAddress.postalCode")
    @Mapping(source = "restaurantAddress.addressAddressStreet", target = "restaurantAddress.address")
    @Mapping(source = "restaurantSchedule", target = "schedule")
    RestaurantEntity mapToEntity(Restaurant restaurant);

    default List<RestaurantOpeningTime> mapFromEntityRestaurantOpeningTimeList(List<RestaurantOpeningTimeEntity> entityList){
        List<RestaurantOpeningTime> openingTimeList = new ArrayList<>();

        for (RestaurantOpeningTimeEntity entity : entityList) {
            RestaurantOpeningTime openingTime = mapFromEntityRestaurantOpeningTime(entity);
            openingTimeList.add(openingTime);
        }
        return openingTimeList;
    }

    default RestaurantOpeningTime mapFromEntityRestaurantOpeningTime(RestaurantOpeningTimeEntity entity){
        return RestaurantOpeningTime.builder()
                .openingHour(entity.getOpeningHour().toString())
                .closeHour(entity.getCloseHour().toString())
                .dayOfWeek(entity.getDayOfWeek())
                .build();
    }
}
