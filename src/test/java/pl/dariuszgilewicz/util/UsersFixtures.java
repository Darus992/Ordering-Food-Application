package pl.dariuszgilewicz.util;

import lombok.experimental.UtilityClass;
import pl.dariuszgilewicz.infrastructure.database.entity.AddressEntity;
import pl.dariuszgilewicz.infrastructure.database.entity.CustomerEntity;
import pl.dariuszgilewicz.infrastructure.security.User;
import pl.dariuszgilewicz.infrastructure.security.UserEntity;
import pl.dariuszgilewicz.infrastructure.security.UserRole;

import java.util.Set;

@UtilityClass
public class UsersFixtures {

    public static UserEntity someBusinessUser1(){
        return UserEntity.builder()
                .username("business_user")
                .email("business@business_user.com")
                .password("haslo")
                .active(true)
                .owner(RestaurantOwnerFixtures.someRestaurantOwnerEntity1())
                .roles(Set.of(UserRole.OWNER))
                .build();
    }

    public static UserEntity someBusinessUser2(){
        return UserEntity.builder()
                .username("business_user2")
                .email("business@business_user2.com")
                .password("haslo")
                .active(true)
                .owner(RestaurantOwnerFixtures.someRestaurantOwnerEntity2())
                .roles(Set.of(UserRole.OWNER))
                .build();
    }

    public static UserEntity someCustomerUser1(){
        return UserEntity.builder()
                .username("testowy_Nickname")
                .email("testowy_Nickname@mail.com")
                .password("haslo")
                .active(true)
                .customer(CustomerEntity.builder()
                        .name("Leszek")
                        .surname("Zaradny")
                        .phone("154457147")
                        .address(AddressEntity.builder()
                                .city("Warszawa")
                                .district("Testowy")
                                .postalCode("22-220")
                                .address("Kolejna ulica 100")
                                .build())
                        .build())
                .roles(Set.of(UserRole.CUSTOMER))
                .build();
    }

    public static User someMappedCustomerUser1(){
        return User.builder()
                .username("testowy_Nickname")
                .email("testowy_Nickname@mail.com")
                .password("encodedPassword")
                .role(UserRole.CUSTOMER)
                .owner(null)
                .build();
    }

    public static User someMappedBusinessUser1(){
        return User.builder()
                .username("business_user")
                .email("business@business_user.com")
                .password("haslo")
                .role(UserRole.OWNER)
                .owner(RestaurantOwnerFixtures.someRestaurantOwner1())
                .build();
    }

    public static User someMappedBusinessUser2(){
        return User.builder()
                .username("business_user2")
                .email("business@business_user2.com")
                .password("haslo")
                .role(UserRole.OWNER)
                .owner(RestaurantOwnerFixtures.someRestaurantOwner2())
                .build();
    }
}
