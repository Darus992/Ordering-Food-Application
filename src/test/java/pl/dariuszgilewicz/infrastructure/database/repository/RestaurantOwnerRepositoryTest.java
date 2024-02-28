package pl.dariuszgilewicz.infrastructure.database.repository;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.dariuszgilewicz.infrastructure.database.entity.RestaurantOwnerEntity;
import pl.dariuszgilewicz.infrastructure.database.repository.jpa.RestaurantOwnerJpaRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static pl.dariuszgilewicz.util.RestaurantOwnerFixtures.someRestaurantOwnerEntity1;

@ExtendWith(MockitoExtension.class)
class RestaurantOwnerRepositoryTest {

    @InjectMocks
    private RestaurantOwnerRepository restaurantOwnerRepository;

    @Mock
    private RestaurantOwnerJpaRepository restaurantOwnerJpaRepository;

    @Test
    void findRestaurantOwnerEntityByPesel_shouldWorkCorrectly() {
        //  given
        RestaurantOwnerEntity expectedOwner = someRestaurantOwnerEntity1();
        String pesel = expectedOwner.getPesel();
        when(restaurantOwnerJpaRepository.findByPesel(pesel)).thenReturn(Optional.of(expectedOwner));

        //  when
        RestaurantOwnerEntity ownerResult = restaurantOwnerRepository.findRestaurantOwnerEntityByPesel(pesel);

        //  then
        assertEquals(expectedOwner, ownerResult);
    }

    @Test
    void findRestaurantOwnerEntityByPesel_shouldThrowException(){
        //  given
        String pesel = "91014452879";
        when(restaurantOwnerJpaRepository.findByPesel(pesel)).thenReturn(Optional.empty());

        //  when
        //  then
        assertThatThrownBy(() -> restaurantOwnerRepository.findRestaurantOwnerEntityByPesel(pesel))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Restaurant Owner with pesel: [%s] not found".formatted(pesel));
    }
}