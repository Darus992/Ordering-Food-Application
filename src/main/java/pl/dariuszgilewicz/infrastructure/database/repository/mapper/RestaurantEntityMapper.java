package pl.dariuszgilewicz.infrastructure.database.repository.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.dariuszgilewicz.infrastructure.database.entity.*;
import pl.dariuszgilewicz.infrastructure.model.*;
import pl.dariuszgilewicz.infrastructure.request_form.BusinessRequestForm;
import pl.dariuszgilewicz.infrastructure.request_form.RestaurantRequestForm;
import pl.dariuszgilewicz.infrastructure.util.ImageConverter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class RestaurantEntityMapper {
    private FoodMenuEntityMapper foodMenuEntityMapper;
    private AddressEntityMapper addressEntityMapper;
    private RestaurantOpeningTimeEntityMapper restaurantOpeningTimeEntityMapper;

    public Restaurant mapFromEntity(RestaurantEntity entity){
        return Restaurant.builder()
                .restaurantImageCard(ImageConverter.convertFromBytes(entity.getRestaurantImageCard()))
                .restaurantImageHeader(ImageConverter.convertFromBytes(entity.getRestaurantImageHeader()))
                .restaurantName(entity.getRestaurantName())
                .restaurantPhone(entity.getPhone())
                .restaurantEmail(entity.getEmail())
                .foodMenu(foodMenuEntityMapper.mapFromEntity(entity.getFoodMenu()))
                .restaurantAddress(addressEntityMapper.mapFromEntity(entity.getRestaurantAddress()))
                .restaurantOpeningTime(restaurantOpeningTimeEntityMapper.mapFromEntity(entity.getRestaurantOpeningTime()))
                .customerOrdersNumbers(entity.getCustomerOrders().stream()
                        .map(OrdersEntity::getOrderNumber)
                        .collect(Collectors.toList()))
                .build();
    }

    public RestaurantEntity mapToEntity(Restaurant restaurant){
        return RestaurantEntity.builder()
                .restaurantImageCard(ImageConverter.convertToBytes(restaurant.getRestaurantImageCard()))
                .restaurantImageHeader(ImageConverter.convertToBytes(restaurant.getRestaurantImageHeader()))
                .restaurantName(restaurant.getRestaurantName())
                .phone(restaurant.getRestaurantPhone())
                .email(restaurant.getRestaurantEmail())
                .foodMenu(foodMenuEntityMapper.mapToEntity(restaurant.getFoodMenu()))
                .restaurantAddress(addressEntityMapper.mapToEntity(restaurant.getRestaurantAddress()))
                .restaurantOpeningTime(restaurantOpeningTimeEntityMapper.mapToEntity(restaurant.getRestaurantOpeningTime()))
                .build();
    }

    public List<Restaurant> mapFromEntityList(List<RestaurantEntity> entities){
        return entities.stream()
                .map(this::mapFromEntity)
                .toList();
    }

    public RestaurantEntity mapFromBusinessRequest(BusinessRequestForm requestForm) throws IOException {
        return RestaurantEntity.builder()
                .restaurantImageCard(requestForm.getRestaurantImageCard().getBytes())
                .restaurantImageHeader(requestForm.getRestaurantImageHeader().getBytes())
                .restaurantName(requestForm.getRestaurantName())
                .phone(requestForm.getRestaurantPhone())
                .email(requestForm.getRestaurantEmail())
                .foodMenu(FoodMenuEntity.builder()
                        .menuName(requestForm.getRestaurantName() + " Menu")
                        .build())
                .restaurantAddress(addressEntityMapper.mapFromBusinessRequest(requestForm))
                .restaurantOpeningTime(restaurantOpeningTimeEntityMapper.mapFromBusinessRequest(requestForm))
                .build();
    }

    public RestaurantEntity mapFromRestaurantRequest(RestaurantRequestForm requestForm, RestaurantOwnerEntity owner) {
        return RestaurantEntity.builder()
                .restaurantImageCard(ImageConverter.convertFileToBytes(requestForm.getRestaurantImageCard()))
                .restaurantImageHeader(ImageConverter.convertFileToBytes(requestForm.getRestaurantImageHeader()))
                .restaurantName(requestForm.getRestaurantName())
                .phone(requestForm.getRestaurantPhone())
                .email(requestForm.getRestaurantEmail())
                .foodMenu(FoodMenuEntity.builder()
                        .menuName(requestForm.getRestaurantName() + " Menu")
                        .build())
                .restaurantAddress(addressEntityMapper.mapFromRestaurantRequest(requestForm))
                .restaurantOwner(owner)
                .restaurantOpeningTime(restaurantOpeningTimeEntityMapper.mapFromRestaurantRequest(requestForm))
                .build();
    }

    public List<RestaurantEntity> mapToEntityList(List<Restaurant> restaurants) {
        return restaurants.stream()
                .map(this::mapToEntity)
                .toList();
    }
}
