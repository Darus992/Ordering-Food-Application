package pl.dariuszgilewicz.infrastructure.database.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pl.dariuszgilewicz.infrastructure.database.entity.*;
import pl.dariuszgilewicz.infrastructure.model.*;
import pl.dariuszgilewicz.infrastructure.request_form.BusinessRequestForm;
import pl.dariuszgilewicz.infrastructure.request_form.CustomerRequestForm;
import pl.dariuszgilewicz.infrastructure.security.UserEntity;
import pl.dariuszgilewicz.infrastructure.security.User;
import pl.dariuszgilewicz.infrastructure.security.UserRole;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserEntityMapper {

    default UserEntity mapToEntity(User user) {
        return UserEntity.builder()
                .userName(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .active(true)
                .roles(Set.of(user.getRole()))
                .build();
    }

    default User mapFromEntityOwner(UserEntity entity){
        return User.builder()
                .username(entity.getUserName())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .role(entity.getRoles().stream().findFirst().get())
                .restaurantOwner(RestaurantOwner.builder()
                        .name(entity.getOwner().getName())
                        .surname(entity.getOwner().getSurname())
                        .pesel(entity.getOwner().getPesel())
                        .restaurants(mapRestaurantFromListEntity(entity.getOwner().getRestaurants()))
                        .build())
                .build();
    }

    default User mapFromEntityCustomer(UserEntity entity){
        return User.builder()
                .username(entity.getUserName())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .role(entity.getRoles().stream().findFirst().get())
                .customer(Customer.builder()
                        .name(entity.getCustomer().getName())
                        .surname(entity.getCustomer().getSurname())
                        .phone(entity.getCustomer().getPhone())
                        .address(Address.builder()
                                .city(entity.getCustomer().getAddress().getCity())
                                .district(entity.getCustomer().getAddress().getCity())
                                .postalCode(entity.getCustomer().getAddress().getPostalCode())
                                .addressStreet(entity.getCustomer().getAddress().getAddress())
                                .build())
                        .customerOrders(mapCustomerOrdersFromEntity(entity.getCustomer().getCustomerOrders()))
                        .build())
                .build();
    }

    default List<Orders> mapCustomerOrdersFromEntity(List<OrdersEntity> entities){
        List<Orders> result = new ArrayList<>();
        for (OrdersEntity entity : entities){
            Orders order = mapOrderEntityToOrder(entity);
            result.add(order);
        }
        return result;
    }


    default Orders mapOrderEntityToOrder(OrdersEntity entity){
        return Orders.builder()
                .orderNumber(entity.getOrderNumber())
                .status(entity.getStatus())
                .orderNotes(entity.getOrderNotes())
                .receivedDateTime(returnDateTimeIfExist(entity.getReceivedDateTime()))
                .expectedDeliveryDateTime(returnDateTimeIfExist(entity.getExpectedDeliveryDateTime()))
                .completedDateTime(returnDateTimeIfExist(entity.getCompletedDateTime()))
                .orderRequest(mapOrderRequest(entity.getOrderRequest()))
                .totalPrice(entity.getTotalPrice())
                .restaurant(mapRestaurantFromEntity(entity.getRestaurant()))
                .build();
    }

    default String returnDateTimeIfExist(OffsetDateTime dateTime){
        if(dateTime != null){
            return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }else {
            return "";
        }
    }

    default Map<Food, Integer> mapOrderRequest(Map<FoodEntity, Integer> orderRequest){
        Map<Food, Integer> resultMap = new HashMap<>();
        Iterator<Map.Entry<FoodEntity, Integer>> iterator = orderRequest.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<FoodEntity, Integer> entry = iterator.next();
            Integer entryValue = entry.getValue();
            FoodEntity entryKey = entry.getKey();
            Food food = Food.builder()
                    .foodId(entryKey.getFoodId())
                    .category(entryKey.getCategory())
                    .name(entryKey.getName())
                    .description(entryKey.getDescription())
                    .price(entryKey.getPrice())
                    .build();
            resultMap.put(food, entryValue);
        }
        return resultMap;
    }

    default List<Restaurant> mapRestaurantFromListEntity(List<RestaurantEntity> entities){
        List<Restaurant> restaurants = new ArrayList<>();
        for (RestaurantEntity entity : entities) {
            Restaurant restaurant = mapRestaurantFromEntity(entity);
            restaurants.add(restaurant);
        }
        return restaurants;
    }

    @Mapping(source = "restaurantName", target = "restaurantName")
    @Mapping(source = "phone", target = "restaurantPhone")
    @Mapping(source = "email", target = "restaurantEmail")
    @Mapping(source = "restaurantAddress.city", target = "restaurantAddress.city")
    @Mapping(source = "restaurantAddress.district", target = "restaurantAddress.district")
    Restaurant mapRestaurantFromEntity(RestaurantEntity entity);



    default UserEntity mapFromCustomerRequest(CustomerRequestForm customerRequestForm){
        return UserEntity.builder()
                .userName(customerRequestForm.getUsername())
                .email(customerRequestForm.getUserEmail())
                .password(customerRequestForm.getUserPassword())
                .active(true)
                .customer(CustomerEntity.builder()
                        .name(customerRequestForm.getCustomerName())
                        .surname(customerRequestForm.getCustomerSurname())
                        .phone(customerRequestForm.getCustomerPhone())
                        .address(AddressEntity.builder()
                                .city(customerRequestForm.getCustomerAddressCity())
                                .district(customerRequestForm.getCustomerAddressDistrict())
                                .postalCode(customerRequestForm.getCustomerAddressPostalCode())
                                .address(customerRequestForm.getCustomerAddressStreet())
                                .build())
                        .build())
                .roles(Set.of(UserRole.CUSTOMER))
                .build();
    }

    default UserEntity mapFromBusinessRequest(BusinessRequestForm businessRequestForm){
        return UserEntity.builder()
                .userName(businessRequestForm.getUsername())
                .email(businessRequestForm.getUserEmail())
                .password(businessRequestForm.getUserPassword())
                .active(true)
                .owner(RestaurantOwnerEntity.builder()
                        .name(businessRequestForm.getOwnerName())
                        .surname(businessRequestForm.getOwnerSurname())
                        .pesel(businessRequestForm.getOwnerPesel())
                        .build())
                .roles(Set.of(UserRole.OWNER))
                .build();
    }
}
