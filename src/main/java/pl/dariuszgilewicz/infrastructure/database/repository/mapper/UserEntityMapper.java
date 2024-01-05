package pl.dariuszgilewicz.infrastructure.database.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pl.dariuszgilewicz.infrastructure.database.entity.RestaurantEntity;
import pl.dariuszgilewicz.infrastructure.model.Restaurant;
import pl.dariuszgilewicz.infrastructure.security.UserEntity;
import pl.dariuszgilewicz.infrastructure.security.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserEntityMapper {

    default UserEntity mapToEntity(User user) {
        return UserEntity.builder()
                .userName(user.getUserName())
                .email(user.getEmail())
                .password(user.getPassword())
                .active(true)
                .roles(Set.of(user.getRole()))
                .build();
    }

    default User mapFromEntity(UserEntity entity){
        return User.builder()
                .userName(entity.getUserName())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .role(entity.getRoles().stream().findFirst().get())
                .restaurants(mapRestaurantFromListEntity(entity.getRestaurants()))
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
    @Mapping(source = "schedule", target = "restaurantSchedule")
    Restaurant mapRestaurantFromEntity(RestaurantEntity entity);
}
