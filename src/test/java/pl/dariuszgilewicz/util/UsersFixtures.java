package pl.dariuszgilewicz.util;

import lombok.experimental.UtilityClass;
import pl.dariuszgilewicz.infrastructure.security.User;
import pl.dariuszgilewicz.infrastructure.security.UserEntity;
import pl.dariuszgilewicz.infrastructure.security.UserRole;

import java.util.Set;

import static pl.dariuszgilewicz.util.CustomerFixtures.*;
import static pl.dariuszgilewicz.util.RestaurantOwnerFixtures.*;

@UtilityClass
public class UsersFixtures {

    public static UserEntity someBusinessUserEntity1() {
        return UserEntity.builder()
                .username("business_user")
                .email("business@business_user.com")
                .password("haslo")
                .active(true)
                .owner(someRestaurantOwnerEntity1())
                .roles(Set.of(UserRole.OWNER))
                .build();
    }

    public static UserEntity someBusinessUserEntity2() {
        return UserEntity.builder()
                .username("business_user2")
                .email("business@business_user2.com")
                .password("haslo")
                .active(true)
                .owner(someRestaurantOwnerEntity2())
                .roles(Set.of(UserRole.OWNER))
                .build();
    }

    public static UserEntity someCustomerUserEntity1() {
        return UserEntity.builder()
                .username("testowy_customer")
                .email("testowy_customer@mail.com")
                .password("haslo")
                .active(true)
                .customer(someCustomerEntity1())
                .roles(Set.of(UserRole.CUSTOMER))
                .build();
    }

    public static User someCustomerUserModel1() {
        return User.builder()
                .username("testowy_customer")
                .email("testowy_customer@mail.com")
                .password("haslo")    //  encodedPassword
                .role(UserRole.CUSTOMER)
                .owner(null)
                .customer(someCustomerModel1())
                .build();
    }

    public static User someErrorCustomerUserModel1() {
        return User.builder()
                .username("")
                .email("testowy_customer@mail.com")
                .password("pass")
                .role(UserRole.CUSTOMER)
                .owner(null)
                .customer(someErrorCustomerModel1())
                .build();
    }

    public static User someErrorCustomerUserModel2() {
        return User.builder()
                .username("nick")
                .email("nick_customer@email.com")
                .password("pass")
                .role(UserRole.CUSTOMER)
                .owner(null)
                .customer(someErrorCustomerModel2())
                .build();
    }

    public static User someBusinessUserModel1() {
        return User.builder()
                .username("business_user")
                .email("business@business_user.com")
                .password("haslo")
                .role(UserRole.OWNER)
                .owner(someRestaurantOwnerModel1())
                .build();
    }

    public static User someBusinessUserModel2() {
        return User.builder()
                .username("business_user2")
                .email("business@business_user2.com")
                .password("haslo")
                .role(UserRole.OWNER)
                .owner(someRestaurantOwnerModel3())
                .build();
    }
}
