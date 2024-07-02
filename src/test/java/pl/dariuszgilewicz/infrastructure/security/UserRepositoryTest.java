package pl.dariuszgilewicz.infrastructure.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static pl.dariuszgilewicz.util.BusinessRequestFormFixtures.someBusinessRequestForm1;

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
