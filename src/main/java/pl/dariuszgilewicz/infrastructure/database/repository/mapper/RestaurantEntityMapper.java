package pl.dariuszgilewicz.infrastructure.database.repository.mapper;

import org.mapstruct.*;
import pl.dariuszgilewicz.infrastructure.database.entity.AddressEntity;
import pl.dariuszgilewicz.infrastructure.database.entity.RestaurantEntity;
import pl.dariuszgilewicz.infrastructure.database.entity.RestaurantOpeningTimeEntity;
import pl.dariuszgilewicz.infrastructure.database.entity.RestaurantOwnerEntity;
import pl.dariuszgilewicz.infrastructure.model.*;
import pl.dariuszgilewicz.infrastructure.request_form.BusinessRequestForm;
import pl.dariuszgilewicz.infrastructure.request_form.RestaurantRequestForm;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RestaurantEntityMapper {

    default Restaurant mapFromEntity(RestaurantEntity entity){
        return Restaurant.builder()
                .restaurantName(entity.getRestaurantName())
                .restaurantPhone(entity.getPhone())
                .restaurantEmail(entity.getEmail())
                .restaurantAddress(Address.builder()
                        .addressCity(entity.getRestaurantAddress().getCity())
                        .addressDistrict(entity.getRestaurantAddress().getDistrict())
                        .addressPostalCode(entity.getRestaurantAddress().getPostalCode())
                        .addressAddressStreet(entity.getRestaurantAddress().getAddress())
                        .build())
                .restaurantOpeningTime(RestaurantOpeningTime.builder()
                        .openingHour(entity.getRestaurantOpeningTime().getOpeningHour().toString())
                        .closeHour(entity.getRestaurantOpeningTime().getCloseHour().toString())
                        .dayOfWeekFrom(entity.getRestaurantOpeningTime().getDayOfWeekFrom())
                        .dayOfWeekTill(entity.getRestaurantOpeningTime().getDayOfWeekTill())
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
    RestaurantEntity mapToEntity(Restaurant restaurant);

    default RestaurantEntity mapFromBusinessRequest(BusinessRequestForm businessRequestForm, RestaurantOwnerEntity owner){
        return RestaurantEntity.builder()
                .restaurantName(businessRequestForm.getRestaurantName())
                .phone(businessRequestForm.getRestaurantPhone())
                .email(businessRequestForm.getRestaurantEmail())
                .restaurantAddress(AddressEntity.builder()
                        .city(businessRequestForm.getRestaurantAddressCity())
                        .district(businessRequestForm.getRestaurantAddressDistrict())
                        .postalCode(businessRequestForm.getRestaurantAddressPostalCode())
                        .address(businessRequestForm.getRestaurantAddressStreet())
                        .build())
                .restaurantOwner(owner)
                .restaurantOpeningTime(RestaurantOpeningTimeEntity.builder()
                        .openingHour(LocalTime.parse(businessRequestForm.getOpeningHour() + ":00", DateTimeFormatter.ISO_LOCAL_TIME))
                        .closeHour(LocalTime.parse(businessRequestForm.getCloseHour() + ":00", DateTimeFormatter.ISO_LOCAL_TIME))
                        .dayOfWeekFrom(businessRequestForm.getDayOfWeekFrom())
                        .dayOfWeekTill(businessRequestForm.getDayOfWeekTill())
                        .build())
                .build();
    }

    default RestaurantEntity mapFromRestaurantRequest(RestaurantRequestForm restaurantRequestForm, RestaurantOwnerEntity ownerEntity){
        return RestaurantEntity.builder()
                .restaurantName(restaurantRequestForm.getRestaurantName())
                .phone(restaurantRequestForm.getRestaurantPhone())
                .email(restaurantRequestForm.getRestaurantEmail())
                .restaurantAddress(AddressEntity.builder()
                        .city(restaurantRequestForm.getRestaurantAddressCity())
                        .district(restaurantRequestForm.getRestaurantAddressDistrict())
                        .postalCode(restaurantRequestForm.getRestaurantAddressPostalCode())
                        .address(restaurantRequestForm.getRestaurantAddressStreet())
                        .build())
                .restaurantOwner(ownerEntity)
                .restaurantOpeningTime(RestaurantOpeningTimeEntity.builder()
                        .openingHour(LocalTime.parse(restaurantRequestForm.getOpeningHour() + ":00", DateTimeFormatter.ISO_LOCAL_TIME))
                        .closeHour(LocalTime.parse(restaurantRequestForm.getCloseHour() + ":00", DateTimeFormatter.ISO_LOCAL_TIME))
                        .dayOfWeekFrom(restaurantRequestForm.getDayOfWeekFrom())
                        .dayOfWeekTill(restaurantRequestForm.getDayOfWeekTill())
                        .build())
                .build();
    }
}
