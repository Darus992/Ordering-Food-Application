package pl.dariuszgilewicz.infrastructure.security;

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
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;
import static pl.dariuszgilewicz.util.BusinessRequestFormFixtures.someBusinessRequestForm1;
import static pl.dariuszgilewicz.util.CustomerRequestFormFixtures.someCustomerRequestForm;
import static pl.dariuszgilewicz.util.UsersFixtures.someBusinessUserEntity1;
import static pl.dariuszgilewicz.util.UsersFixtures.someCustomerUserEntity1;


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
        UserEntity userEntity = someCustomerUserEntity1();
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
        UserEntity businessUser = someBusinessUserEntity1();
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
}