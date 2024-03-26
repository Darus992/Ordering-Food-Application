package pl.dariuszgilewicz.infrastructure.database.repository.mapper;

import org.mapstruct.*;
import pl.dariuszgilewicz.infrastructure.database.entity.*;
import pl.dariuszgilewicz.infrastructure.model.*;
import pl.dariuszgilewicz.infrastructure.request_form.BusinessRequestForm;
import pl.dariuszgilewicz.infrastructure.request_form.RestaurantRequestForm;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RestaurantEntityMapper {

    default Restaurant mapFromEntity(RestaurantEntity entity){
        return Restaurant.builder()
                .restaurantImageCard(entity.getRestaurantImageCard())
                .restaurantName(entity.getRestaurantName())
                .restaurantPhone(entity.getPhone())
                .restaurantEmail(entity.getEmail())
                .foodMenu(FoodMenu.builder()
                        .foodMenuId(entity.getFoodMenu().getFoodMenuId())
                        .foodMenuImage(entity.getFoodMenu().getFoodMenuImage())
                        .foods(mapAllFoodFromEntity(entity.getFoodMenu().getFoods()))
                        .build())
                .restaurantAddress(Address.builder()
                        .city(entity.getRestaurantAddress().getCity())
                        .district(entity.getRestaurantAddress().getDistrict())
                        .postalCode(entity.getRestaurantAddress().getPostalCode())
                        .addressStreet(entity.getRestaurantAddress().getAddress())
                        .build())
                .restaurantOpeningTime(RestaurantOpeningTime.builder()
                        .openingHour(entity.getRestaurantOpeningTime().getOpeningHour().toString())
                        .closeHour(entity.getRestaurantOpeningTime().getCloseHour().toString())
                        .dayOfWeekFrom(entity.getRestaurantOpeningTime().getDayOfWeekFrom())
                        .dayOfWeekTill(entity.getRestaurantOpeningTime().getDayOfWeekTill())
                        .build())
                .customerOrdersNumbers(getCustomerOrdersNumbers(entity.getCustomerOrders()))
                .build();
    }

    default List<Integer> getCustomerOrdersNumbers(List<OrdersEntity> ordersEntities){
        List<Integer> resultList = new ArrayList<>();

        for (OrdersEntity orders : ordersEntities){
            resultList.add(orders.getOrderNumber());
        }
        return resultList;
    }

    default List<Food> mapAllFoodFromEntity(List<FoodEntity> allEntity){
        List<Food> resultList = new ArrayList<>();
        for (FoodEntity foodEntity : allEntity){
            Food food = Food.builder()
                    .foodId(foodEntity.getFoodId())
                    .category(foodEntity.getCategory())
                    .name(foodEntity.getName())
                    .description(foodEntity.getDescription())
                    .price(foodEntity.getPrice())
                    .build();
            resultList.add(food);
        }
        return resultList;
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
    @Mapping(source = "restaurantAddress.city", target = "restaurantAddress.city")
    @Mapping(source = "restaurantAddress.district", target = "restaurantAddress.district")
    @Mapping(source = "restaurantAddress.postalCode", target = "restaurantAddress.postalCode")
    @Mapping(source = "restaurantAddress.addressStreet", target = "restaurantAddress.address")
    RestaurantEntity mapToEntity(Restaurant restaurant);

    default RestaurantEntity mapFromBusinessRequest(BusinessRequestForm businessRequestForm) throws IOException {
        return RestaurantEntity.builder()
                .restaurantImageCard(businessRequestForm.getRestaurantImageCard().getBytes())
                .restaurantName(businessRequestForm.getRestaurantName())
                .phone(businessRequestForm.getRestaurantPhone())
                .email(businessRequestForm.getRestaurantEmail())
                .restaurantAddress(AddressEntity.builder()
                        .city(businessRequestForm.getRestaurantAddressCity())
                        .district(businessRequestForm.getRestaurantAddressDistrict())
                        .postalCode(businessRequestForm.getRestaurantAddressPostalCode())
                        .address(businessRequestForm.getRestaurantAddressStreet())
                        .build())
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
