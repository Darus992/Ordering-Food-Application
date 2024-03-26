package pl.dariuszgilewicz.infrastructure.database.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pl.dariuszgilewicz.infrastructure.database.entity.*;
import pl.dariuszgilewicz.infrastructure.model.*;
import pl.dariuszgilewicz.infrastructure.security.UserEntity;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderEntityMapper {

    default Orders mapFromEntity(OrdersEntity entity) {
        return Orders.builder()
                .orderNumber(entity.getOrderNumber())
                .status(entity.getStatus())
                .orderNotes(entity.getOrderNotes())
                .receivedDateTime(returnDateTimeIfExist(entity.getReceivedDateTime()))
                .expectedDeliveryDateTime(returnDateTimeIfExist(entity.getExpectedDeliveryDateTime()))
                .completedDateTime(returnDateTimeIfExist(entity.getCompletedDateTime()))
                .orderRequest(mapOrderRequestFromEntity(entity.getOrderRequest()))
                .totalPrice(entity.getTotalPrice())
                .customer(mapToCustomer(entity.getCustomer()))
                .restaurant(mapToRestaurant(entity.getRestaurant()))
                .build();
    }

    default String returnDateTimeIfExist(OffsetDateTime dateTime){
        if(dateTime != null){
            return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }else {
            return "";
        }
    }

    default Restaurant mapToRestaurant(RestaurantEntity restaurant) {
        return Restaurant.builder()
                .restaurantName(restaurant.getRestaurantName())
                .restaurantEmail(restaurant.getEmail())
                .restaurantAddress(mapToAddress(restaurant.getRestaurantAddress()))
                .build();
    }

    default Address mapToAddress(AddressEntity restaurantAddress) {
        return Address.builder()
                .city(restaurantAddress.getCity())
                .district(restaurantAddress.getDistrict())
                .postalCode(restaurantAddress.getPostalCode())
                .addressStreet(restaurantAddress.getAddress())
                .build();
    }

    @Mapping(source = "customerOrders", target = "customerOrders", ignore = true)
    Customer mapToCustomer(CustomerEntity customer);

    default OrdersEntity mapToEntity(Orders orders, CustomerEntity customerEntity, UserEntity userEntity, RestaurantEntity restaurantEntity) {
        return OrdersEntity.builder()
                .orderNumber(orders.getOrderNumber())
                .status(orders.getStatus())
                .orderNotes(orders.getOrderNotes())
                .receivedDateTime(OffsetDateTime.parse(orders.getReceivedDateTime()))
                .completedDateTime(OffsetDateTime.parse(orders.getCompletedDateTime()))
                .orderRequest(mapOrderRequestToEntity(orders.getOrderRequest()))
                .totalPrice(orders.getTotalPrice())
                .customer(customerEntity)
                .restaurant(restaurantEntity)
                .build();
    }

    Map<Food, Integer> mapOrderRequestFromEntity(Map<FoodEntity, Integer> orderRequest);

    Map<FoodEntity, Integer> mapOrderRequestToEntity(Map<Food, Integer> orderRequest);
}
