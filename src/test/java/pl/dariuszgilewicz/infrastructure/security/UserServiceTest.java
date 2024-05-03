package pl.dariuszgilewicz.infrastructure.security;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.dariuszgilewicz.infrastructure.database.repository.mapper.UserEntityMapper;
import pl.dariuszgilewicz.infrastructure.model.exception.EntityAlreadyExistAuthenticationException;
import pl.dariuszgilewicz.infrastructure.request_form.BusinessRequestForm;
import pl.dariuszgilewicz.infrastructure.request_form.CustomerRequestForm;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;
import static pl.dariuszgilewicz.util.BusinessRequestFormFixtures.someBusinessRequestForm1;
import static pl.dariuszgilewicz.util.CustomerRequestFormFixtures.someCustomerRequestForm;
import static pl.dariuszgilewicz.util.UsersFixtures.*;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserJpaRepository userJpaRepository;
    @Mock
    private UserEntityMapper userEntityMapper;

    @Test
    @DisplayName("Adding customer user with the same email twice fails")
    void createCustomerUser_shouldThrowException() {
        //  given
        CustomerRequestForm requestForm = someCustomerRequestForm();
        UserEntity userEntity = someCustomerUser1();
        when(userJpaRepository.findByEmail(requestForm.getUserEmail())).thenReturn(Optional.of(userEntity));

        //  when
        //  then
        assertThatThrownBy(() -> userService.createCustomerUser(requestForm))
                .isInstanceOf(EntityAlreadyExistAuthenticationException.class)
                .hasMessageContaining("User with email: [%s] already exist".formatted(requestForm.getUserEmail()));
    }

    @Test
    @DisplayName("Adding customer user to the registry work successfully")
    void createCustomerUser_shouldWorkCorrectly() {
        //  given
        CustomerRequestForm requestForm = someCustomerRequestForm();
        given(userJpaRepository.findByEmail(requestForm.getUserEmail())).willReturn(Optional.empty());

        //  when
        userService.createCustomerUser(requestForm);

        //  then
        then(userRepository).should().createCustomerUser(requestForm);
    }

    @Test
    @DisplayName("Adding business user with the same email twice fails")
    void createBusinessUser_shouldThrowException() {
        //  given
        BusinessRequestForm requestForm = someBusinessRequestForm1();
        UserEntity businessUser = someBusinessUser1();
        when(userJpaRepository.findByEmail(requestForm.getUserEmail())).thenReturn(Optional.of(businessUser));

        //  when
        //  then
        assertThatThrownBy(() -> userService.createBusinessUser(requestForm))
                .isInstanceOf(EntityAlreadyExistAuthenticationException.class)
                .hasMessageContaining("User with email: [%s] already exist".formatted(requestForm.getUserEmail()));
    }

    @Test
    @DisplayName("Adding business user to the registry work successfully")
    void createBusinessUser_shouldWorkCorrectly() {
        //  given
        BusinessRequestForm requestForm = someBusinessRequestForm1();
        given(userJpaRepository.findByEmail(requestForm.getUserEmail())).willReturn(Optional.empty());

        //  when
        userService.createBusinessUser(requestForm);

        //  then
        then(userRepository).should().createBusinessUser(requestForm);
    }

//    @Test
//    @DisplayName("Find and map user entity to user model work successfully")
//    void findUserByUserName_shouldWorkCorrectly() {
//        //  given
//        UserEntity existingUser = someCustomerUser1();
//        User expectedUser = someMappedCustomerUser1();
//        when(userJpaRepository.findByUserName(existingUser.getUsername())).thenReturn(Optional.of(existingUser));
//        when(userEntityMapper.mapFromEntityOwner(existingUser)).thenReturn(expectedUser);
//
//        //  when
////        User userResult = userService.findUserOwnerByUserName(existingUser.getUserName());
//        User userResult = userService.getCurrentUser();
//
//        //  then
//        assertEquals(expectedUser.getUsername(), userResult.getUsername());
//        assertEquals(expectedUser.getEmail(), userResult.getEmail());
//        assertEquals(expectedUser.getPassword(), userResult.getPassword());
//
//    }

//    @Test
//    @DisplayName("Should throw exception user not found by username")
//    void findUserByUserName_shouldThrowExceptionUserNotFoundByUsername(){
//        //  given
//        String notExistingUsername = "Zawisza_Czarny";
//        when(userJpaRepository.findByUserName(notExistingUsername)).thenReturn(Optional.empty());
//
//        //  when
//        //  then
////        assertThatThrownBy(() -> userService.findUserOwnerByUserName(notExistingUsername))
////                .isInstanceOf(EntityNotFoundException.class)
////                .hasMessageContaining("User Entity with username: [%s] not found".formatted(notExistingUsername));
//        assertThatThrownBy(() -> userService.getCurrentUser())
//                .isInstanceOf(EntityNotFoundException.class)
//                .hasMessageContaining("User Entity with username: [%s] not found".formatted(notExistingUsername));
//    }
}