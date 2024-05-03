package pl.dariuszgilewicz.infrastructure.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.dariuszgilewicz.infrastructure.database.repository.RestaurantRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.jpa.RestaurantJpaRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.jpa.RestaurantOwnerJpaRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.mapper.UserEntityMapper;
import pl.dariuszgilewicz.infrastructure.model.exception.EntityAlreadyExistAuthenticationException;
import pl.dariuszgilewicz.infrastructure.request_form.BusinessRequestForm;
import pl.dariuszgilewicz.infrastructure.request_form.CustomerRequestForm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static pl.dariuszgilewicz.util.BusinessRequestFormFixtures.someBusinessRequestForm1;
import static pl.dariuszgilewicz.util.CustomerRequestFormFixtures.someCustomerRequestForm;
import static pl.dariuszgilewicz.util.UsersFixtures.someBusinessUser1;
import static pl.dariuszgilewicz.util.UsersFixtures.someCustomerUser1;

@ExtendWith(MockitoExtension.class)
public class UserRepositoryTest {

    @InjectMocks
    UserRepository userRepository;
    @Mock
    RestaurantRepository restaurantRepository;
    @Mock
    UserEntityMapper userEntityMapper;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    UserJpaRepository userJpaRepository;
    @Mock
    RestaurantJpaRepository restaurantJpaRepository;
    @Mock
    RestaurantOwnerJpaRepository restaurantOwnerJpaRepository;


//    @Test
//    @DisplayName("Should map customer request and save to the database successfully")
//    void createCustomerUser_shouldMapCustomerRequestAndSaveToDatabase() {
//        //  given
//        CustomerRequestForm requestForm = someCustomerRequestForm();
//        UserEntity expectedUserEntity = someCustomerUser1();
//        when(userEntityMapper.mapFromCustomerRequest(requestForm)).thenReturn(expectedUserEntity);
//        when(passwordEncoder.encode(requestForm.getUserPassword())).thenReturn("encodedPassword");
//
//        //  when
//        userRepository.createCustomerUser(requestForm);
//
//        //  then
//        ArgumentCaptor<UserEntity> userEntityCaptor = ArgumentCaptor.forClass(UserEntity.class);
//        verify(userJpaRepository).save(userEntityCaptor.capture());
//
//        UserEntity savedUserEntity = userEntityCaptor.getValue();
//
//        assertEquals(expectedUserEntity.getUsername(), savedUserEntity.getUsername());
//        assertEquals(expectedUserEntity.getEmail(), savedUserEntity.getEmail());
//        assertEquals(expectedUserEntity.getCustomer(), savedUserEntity.getCustomer());
//
//        verify(passwordEncoder).encode(requestForm.getUserPassword());
//
//    }

//    @Test
//    @DisplayName("Should map business request and save user to the database successfully")
//    void createBusinessUser_shouldMapBusinessRequestAndSaveToDatabase() {
//        //  given
//        BusinessRequestForm requestForm = someBusinessRequestForm1();
//        UserEntity expectedUserEntity = someBusinessUser1();
//        when(restaurantJpaRepository.existsByEmail(requestForm.getRestaurantEmail())).thenReturn(false);
//        when(restaurantOwnerJpaRepository.existsByPesel(requestForm.getOwnerPesel())).thenReturn(false);
//        when(userEntityMapper.mapFromBusinessRequest(requestForm)).thenReturn(expectedUserEntity);
//        when(passwordEncoder.encode(requestForm.getUserPassword())).thenReturn("encodedPassword");
//
//        //  when
//        userRepository.createBusinessUser(requestForm);
//
//        //  then
//        ArgumentCaptor<UserEntity> userEntityCaptor = ArgumentCaptor.forClass(UserEntity.class);
//        verify(userJpaRepository).save(userEntityCaptor.capture());
//
//        UserEntity savedUserEntity = userEntityCaptor.getValue();
//
//        assertEquals(expectedUserEntity.getUsername(), savedUserEntity.getUsername());
//        assertEquals(expectedUserEntity.getEmail(), savedUserEntity.getEmail());
//        assertEquals(expectedUserEntity.getOwner(), savedUserEntity.getOwner());
//
//        verify(passwordEncoder).encode(requestForm.getUserPassword());
//        verify(restaurantRepository).createRestaurantFromBusinessRequest(requestForm, expectedUserEntity.getOwner());
//
//    }

    @Test
    @DisplayName("Should throw exception for existing restaurant email")
    void createBusinessUser_shouldThrowExceptionForExistingRestaurantEmail() {
        //  given
        BusinessRequestForm requestForm = someBusinessRequestForm1();
        when(restaurantJpaRepository.existsByEmail(requestForm.getRestaurantEmail())).thenReturn(true);

        //  when
        //  then
        assertThrows(EntityAlreadyExistAuthenticationException.class, () -> userRepository.createBusinessUser(requestForm));

    }

    @Test
    @DisplayName("Should throw exception for existing owner pesel")
    void createBusinessUser_shouldThrowExceptionForExistingOwnerPesel() {
        //  given
        BusinessRequestForm requestForm = someBusinessRequestForm1();
        when(restaurantJpaRepository.existsByEmail(requestForm.getRestaurantEmail())).thenReturn(false);
        when(restaurantOwnerJpaRepository.existsByPesel(requestForm.getOwnerPesel())).thenReturn(true);

        //  when
        //  then
        assertThrows(EntityAlreadyExistAuthenticationException.class, () -> userRepository.createBusinessUser(requestForm));

    }

}
