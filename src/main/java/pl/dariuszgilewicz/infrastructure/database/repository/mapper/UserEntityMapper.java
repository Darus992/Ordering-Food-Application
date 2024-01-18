package pl.dariuszgilewicz.infrastructure.database.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pl.dariuszgilewicz.infrastructure.database.entity.*;
import pl.dariuszgilewicz.infrastructure.model.Restaurant;
import pl.dariuszgilewicz.infrastructure.model.RestaurantOwner;
import pl.dariuszgilewicz.infrastructure.request_form.BusinessRequestForm;
import pl.dariuszgilewicz.infrastructure.request_form.CustomerRequestForm;
import pl.dariuszgilewicz.infrastructure.security.UserEntity;
import pl.dariuszgilewicz.infrastructure.security.User;
import pl.dariuszgilewicz.infrastructure.security.UserRole;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

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

    default User mapFromEntity(UserEntity entity){
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

    default List<Restaurant> mapRestaurantFromListEntity(List<RestaurantEntity> entities){
        List<Restaurant> restaurants = new ArrayList<>();
        for (RestaurantEntity entity : entities) {
            Restaurant restaurant = mapRestaurantFromEntity(entity);
            restaurants.add(restaurant);
        }
        return restaurants;
    }

    @Mapping(source = "phone", target = "restaurantPhone")
    @Mapping(source = "email", target = "restaurantEmail")
    @Mapping(source = "restaurantAddress.city", target = "restaurantAddress.addressCity")
    @Mapping(source = "restaurantAddress.district", target = "restaurantAddress.addressDistrict")
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
