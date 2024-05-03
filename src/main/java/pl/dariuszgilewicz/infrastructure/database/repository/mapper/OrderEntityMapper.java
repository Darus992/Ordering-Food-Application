package pl.dariuszgilewicz.infrastructure.database.repository.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.dariuszgilewicz.infrastructure.database.entity.*;
import pl.dariuszgilewicz.infrastructure.model.*;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class OrderEntityMapper {

    private FoodEntityMapper foodEntityMapper;
    private RestaurantEntityMapper restaurantEntityMapper;
    private AddressEntityMapper addressEntityMapper;

    public Orders mapFromEntity(OrdersEntity entity){
        Customer customer = Customer.builder()
                .name(entity.getCustomer().getName())
                .surname(entity.getCustomer().getSurname())
                .phone(entity.getCustomer().getPhone())
                .address(addressEntityMapper.mapFromEntity(entity.getCustomer().getAddress()))
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
            .restaurant(restaurantEntityMapper.mapFromEntity(entity.getRestaurant()))
            .build();
}

    public Orders mapFromEntityWithoutRestaurant(OrdersEntity entity){
        Customer customer = Customer.builder()
                .name(entity.getCustomer().getName())
                .surname(entity.getCustomer().getSurname())
                .phone(entity.getCustomer().getPhone())
                .address(addressEntityMapper.mapFromEntity(entity.getCustomer().getAddress()))
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
                .build();
    }

    public List<Orders> mapFromEntityList(List<OrdersEntity> entities){
        return entities.stream()
                .map(this::mapFromEntity)
                .toList();
    }

    public List<Orders> mapFromEntityListWithoutRestaurant(List<OrdersEntity> entities){
        return entities.stream()
                .map(this::mapFromEntityWithoutRestaurant)
                .toList();
    }

    private String returnDateTimeIfExist(OffsetDateTime dateTime){
        return dateTime != null? dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "";
    }

    public Map<Food, Integer> mapOrderRequestFromEntity(Map<FoodEntity, Integer> map){
        return map.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> foodEntityMapper.mapFromEntity(entry.getKey()),
                        Map.Entry::getValue
                ));
    }

    public Map<FoodEntity, Integer> mapOrderRequestToEntity(Map<Food, Integer> map){
        return map.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> foodEntityMapper.mapToEntity(entry.getKey()),
                        Map.Entry::getValue
                ));
    }
}
