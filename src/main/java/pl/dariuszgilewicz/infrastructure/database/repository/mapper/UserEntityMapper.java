package pl.dariuszgilewicz.infrastructure.database.repository.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.dariuszgilewicz.infrastructure.database.entity.CustomerEntity;
import pl.dariuszgilewicz.infrastructure.database.entity.RestaurantOwnerEntity;
import pl.dariuszgilewicz.infrastructure.model.Customer;
import pl.dariuszgilewicz.infrastructure.model.RestaurantOwner;
import pl.dariuszgilewicz.infrastructure.request_form.BusinessRequestForm;
import pl.dariuszgilewicz.infrastructure.request_form.CustomerRequestForm;
import pl.dariuszgilewicz.infrastructure.request_form.RequestForm;
import pl.dariuszgilewicz.infrastructure.security.UserEntity;
import pl.dariuszgilewicz.infrastructure.security.User;
import pl.dariuszgilewicz.infrastructure.security.UserRole;

import java.util.*;

@Component
@AllArgsConstructor
public class UserEntityMapper {

    private RestaurantOwnerEntityMapper restaurantOwnerEntityMapper;
    private CustomerEntityMapper customerEntityMapper;

    public Optional<User> mapFromEntityToOptionalUser(UserEntity entity){

        User user = User.builder()
                .username(entity.getUsername())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .role(entity.getRoles().stream()
                        .findFirst().orElseThrow(
                                () -> new NullPointerException("User role not found")
                        ))
                .build();

        if(entity.getCustomer() != null){
            Customer customer = customerEntityMapper.mapFromEntity(entity.getCustomer());
            user.setCustomer(customer);
        }else if(entity.getOwner() != null){
            RestaurantOwner owner = restaurantOwnerEntityMapper.mapFromEntity(entity.getOwner());
            user.setOwner(owner);
        }

        return Optional.of(user);
    }

    public UserEntity mapFromRequestForm(RequestForm form) {
        UserEntity.UserEntityBuilder builder = UserEntity.builder()
                .username(form.getUsername())
                .email(form.getUserEmail())
                .password(form.getUserPassword())
                .active(true);

        if (form instanceof BusinessRequestForm) {
            builder.owner(restaurantOwnerEntityMapper.mapFromBusinessRequest((BusinessRequestForm) form));
            builder.roles(Set.of(UserRole.OWNER));
        } else if (form instanceof CustomerRequestForm) {
            builder.customer(customerEntityMapper.mapFromCustomerRequest((CustomerRequestForm) form));
            builder.roles(Set.of(UserRole.CUSTOMER));
        } else {
            throw new IllegalArgumentException("Unsupported request form type");
        }

        return builder.build();
    }

    public UserEntity mapToEntity(User user) {
        UserEntity result = UserEntity.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .roles(Set.of(user.getRole()))
                .build();

        if(user.getOwner() != null){
            RestaurantOwnerEntity ownerEntity = restaurantOwnerEntityMapper.mapToEntity(user.getOwner());
            result.setOwner(ownerEntity);
        } else if (user.getCustomer() != null){
            CustomerEntity customerEntity = customerEntityMapper.mapToEntity(user.getCustomer());
            result.setCustomer(customerEntity);
        }
        return result;
    }
}
