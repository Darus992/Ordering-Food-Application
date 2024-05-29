package pl.dariuszgilewicz.infrastructure.database.repository.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.dariuszgilewicz.infrastructure.database.entity.*;
import pl.dariuszgilewicz.infrastructure.model.*;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class OrderEntityMapper {

    private FoodEntityMapper foodEntityMapper;
//    private RestaurantEntityMapper restaurantEntityMapper;
    private AddressEntityMapper addressEntityMapper;

    public Orders mapFromEntity(OrdersEntity entity){
        Customer customer = Customer.builder()
                .name(entity.getCustomer().getName())
                .surname(entity.getCustomer().getSurname())
                .phone(entity.getCustomer().getPhone())
                .address(addressEntityMapper.mapFromEntity(entity.getCustomer().getAddress()))
                .build();

        Restaurant restaurant = Restaurant.builder()
                .restaurantName(entity.getRestaurant().getRestaurantName())
                .restaurantPhone(entity.getRestaurant().getPhone())
                .restaurantEmail(entity.getRestaurant().getEmail())
                .restaurantAddress(addressEntityMapper.mapFromEntity(entity.getRestaurant().getRestaurantAddress()))
                .build();

        return Orders.builder()
            .orderNumber(entity.getOrderNumber())
            .status(entity.getStatus())
            .orderNotes(entity.getOrderNotes())
            .receivedDateTime(returnDateTimeIfExist(entity.getReceivedDateTime()))
            .completedDateTime(returnDateTimeIfExist(entity.getCompletedDateTime()))
            .foods(mapOrderRequestFromEntity(entity.getOrderFoods()))
            .totalPrice(entity.getTotalPrice())
            .customer(customer)
            .restaurant(restaurant)
//            .restaurant(restaurantEntityMapper.mapFromEntity(entity.getRestaurant()))
            .build();
}

//    public OrdersEntity mapToEntity(Orders order) {
//        CustomerEntity customerEntity = CustomerEntity.builder()
//                .name(order.getCustomer().getName())
//                .surname(order.getCustomer().getSurname())
//                .phone(order.getCustomer().getPhone())
//                .address(addressEntityMapper.mapToEntity(order.getCustomer().getAddress()))
//                .build();
//
//        return OrdersEntity.builder()
//                .orderNumber(order.getOrderNumber())
//                .status(order.getStatus())
//                .orderNotes(order.getOrderNotes())
//                .receivedDateTime(returnDateTime(order.getReceivedDateTime()))
//                .completedDateTime(returnDateTime(order.getCompletedDateTime()))
//                .orderFoods(mapOrderRequestToEntity(order.getFoods()))
//                .totalPrice(order.getTotalPrice())
//                .customer(customerEntity)
//                .restaurant(restaurantEntityMapper.mapToEntity(order.getRestaurant()))
//                .build();
//    }


    public List<Orders> mapFromEntityList(List<OrdersEntity> entities){
        return entities.stream()
                .map(this::mapFromEntity)
                .toList();
    }

    private String returnDateTimeIfExist(OffsetDateTime dateTime){
        return dateTime != null ? dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "";
    }

//    private OffsetDateTime returnDateTime(String dateTime) {
//        return OffsetDateTime.parse(dateTime);
//    }

    private Map<Food, Integer> mapOrderRequestFromEntity(Map<FoodEntity, Integer> map){
        return map.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> foodEntityMapper.mapFromEntity(entry.getKey()),
                        Map.Entry::getValue
                ));
    }

//    private Map<FoodEntity, Integer> mapOrderRequestToEntity(Map<Food, Integer> map){
//        return map.entrySet().stream()
//                .collect(Collectors.toMap(
//                        entry -> foodEntityMapper.mapToEntity(entry.getKey()),
//                        Map.Entry::getValue
//                ));
//    }
}
