package pl.dariuszgilewicz.infrastructure.database.repository.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.dariuszgilewicz.infrastructure.request_form.BusinessRequestForm;
import pl.dariuszgilewicz.infrastructure.request_form.CustomerRequestForm;
import pl.dariuszgilewicz.infrastructure.request_form.RequestForm;
import pl.dariuszgilewicz.infrastructure.security.User;
import pl.dariuszgilewicz.infrastructure.security.UserEntity;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static pl.dariuszgilewicz.util.BusinessRequestFormFixtures.*;
import static pl.dariuszgilewicz.util.CustomerRequestFormFixtures.*;
import static pl.dariuszgilewicz.util.UsersFixtures.*;

@ExtendWith(MockitoExtension.class)
class UserEntityMapperTest {

    @InjectMocks
    private UserEntityMapper userEntityMapper;

    @Mock
    private RestaurantOwnerEntityMapper restaurantOwnerEntityMapper;
    @Mock
    private CustomerEntityMapper customerEntityMapper;


    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @ParameterizedTest
    @MethodSource("provideUserOptionalsData")
    void mapFromEntityToOptionalUser_shouldWorkSuccessfully(UserEntity userEntity, Optional<User> expectedUser) {
        //  given
        if(expectedUser.isPresent()){
            if(userEntity.getCustomer() != null){
                when(customerEntityMapper.mapFromEntity(userEntity.getCustomer())).thenReturn(expectedUser.get().getCustomer());
            } else if (userEntity.getOwner() != null) {
                when(restaurantOwnerEntityMapper.mapFromEntity(userEntity.getOwner())).thenReturn(expectedUser.get().getOwner());
            }
        }

        //  when
        Optional<User> resultOptionalUser = userEntityMapper.mapFromEntityToOptionalUser(userEntity);

        //  then
        assertEquals(expectedUser, resultOptionalUser);
    }

    @Test
    void mapFromRequestForm_withBusinessRequestForm_shouldMapCorrectly() {
        // given
        BusinessRequestForm businessRequestForm = someBusinessRequestForm1();
        UserEntity expectedUserEntity = someBusinessUserEntity1();

        when(restaurantOwnerEntityMapper.mapFromBusinessRequest(businessRequestForm)).thenReturn(expectedUserEntity.getOwner());

        // when
        UserEntity resultUserEntity = userEntityMapper.mapFromRequestForm(businessRequestForm);

        // then
        assertEquals(expectedUserEntity, resultUserEntity);
    }

    @Test
    void mapFromRequestForm_withCustomerRequestForm_shouldMapCorrectly() {
        //  given
        CustomerRequestForm customerRequestForm = someCustomerRequestForm();
        UserEntity expectedUserEntity = someCustomerUserEntity1();

        when(customerEntityMapper.mapFromCustomerRequest(customerRequestForm)).thenReturn(expectedUserEntity.getCustomer());

        // when
        UserEntity resultUserEntity = userEntityMapper.mapFromRequestForm(customerRequestForm);

        // then
        assertEquals(expectedUserEntity, resultUserEntity);
    }

    @Test
    void mapFromRequestForm_withUnsupportedFormType_shouldThrowException() {
        // given
        RequestForm unsupportedForm = new RequestForm() {
            // An anonymous class to simulate an unsupported form type
        };

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userEntityMapper.mapFromRequestForm(unsupportedForm);
        });

        assertEquals("Unsupported request form type", exception.getMessage());
    }

    @ParameterizedTest
    @MethodSource("provideUserData")
    void mapToEntity_shouldWorkSuccessfully(User user, UserEntity expectedUserEntity) {
        //  given
        expectedUserEntity.setActive(null);

        if(user.getOwner() != null){
            when(restaurantOwnerEntityMapper.mapToEntity(user.getOwner())).thenReturn(expectedUserEntity.getOwner());
        } else if (user.getCustomer() != null) {
            when(customerEntityMapper.mapToEntity(user.getCustomer())).thenReturn(expectedUserEntity.getCustomer());
        }

        //  when
        UserEntity resultUserEntity = userEntityMapper.mapToEntity(user);

        //  then
        assertEquals(expectedUserEntity, resultUserEntity);
    }

    private static Stream<Arguments> provideUserOptionalsData() {
        return Stream.of(
                Arguments.of((UserEntity) null, Optional.empty()),
                Arguments.of(someBusinessUserEntity1(), Optional.of(someBusinessUserModel1())),
                Arguments.of(someCustomerUserEntity1(), Optional.of(someCustomerUserModel1()))
        );
    }

    private static Stream<Arguments> provideUserData() {
        return Stream.of(
                Arguments.of(someBusinessUserModel1(), someBusinessUserEntity1()),
                Arguments.of(someCustomerUserModel1(), someCustomerUserEntity1())
        );
    }
}