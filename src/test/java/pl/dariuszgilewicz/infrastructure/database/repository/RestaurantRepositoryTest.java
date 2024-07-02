package pl.dariuszgilewicz.infrastructure.database.repository;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.dariuszgilewicz.infrastructure.database.entity.*;
import pl.dariuszgilewicz.infrastructure.database.repository.jpa.RestaurantJpaRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.mapper.RestaurantEntityMapper;
import pl.dariuszgilewicz.infrastructure.request_form.BusinessRequestForm;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static pl.dariuszgilewicz.util.AddressFixtures.someListOfAddressEntity1;
import static pl.dariuszgilewicz.util.BusinessRequestFormFixtures.someBusinessRequestForm1;
import static pl.dariuszgilewicz.util.FoodFixtures.someFoodEntity1;
import static pl.dariuszgilewicz.util.FoodFixtures.someFoodEntity4;
import static pl.dariuszgilewicz.util.FoodMenuFixtures.someFoodMenuEntity2;
import static pl.dariuszgilewicz.util.RestaurantFixtures.*;
import static pl.dariuszgilewicz.util.RestaurantOwnerFixtures.someRestaurantOwnerEntity1;

@ExtendWith(MockitoExtension.class)
class RestaurantRepositoryTest {

    @InjectMocks
    private RestaurantRepository restaurantRepository;

    @Mock
    private RestaurantJpaRepository restaurantJpaRepository;

    @Mock
    private RestaurantEntityMapper restaurantEntityMapper;

    @Mock
    private RestaurantOwnerRepository restaurantOwnerRepository;

    @Test
    void findRestaurantsNearYouByAddress_shouldThrowException() {
        //  given
        List<AddressEntity> addressEntityList = someListOfAddressEntity1();
        when(restaurantJpaRepository.findAllByAddress(addressEntityList)).thenReturn(Optional.empty());

        //  when
        //  then
        assertThatThrownBy(() -> restaurantRepository.findRestaurantsByAddress(addressEntityList))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Not found List of RestaurantEntity by AddressEntity List: [%s]".formatted(addressEntityList));

    }

    @Test
    void createRestaurantFromBusinessRequest_shouldMapFromBusinessRequestAndSaveRestaurantToDatabase() throws IOException {
        //  given
        RestaurantOwnerEntity ownerEntity = someRestaurantOwnerEntity1();
        RestaurantEntity expectedRestaurantEntity = someRestaurantEntity1();
        BusinessRequestForm requestForm = someBusinessRequestForm1();
        when(restaurantEntityMapper.mapFromBusinessRequest(requestForm)).thenReturn(expectedRestaurantEntity);

        //  when
        restaurantRepository.createRestaurantFromBusinessRequest(requestForm, ownerEntity);

        //  then
        ArgumentCaptor<RestaurantEntity> restaurantEntityArgumentCaptor = ArgumentCaptor.forClass(RestaurantEntity.class);
        verify(restaurantJpaRepository).save(restaurantEntityArgumentCaptor.capture());

        RestaurantEntity savedRestaurantEntity = restaurantEntityArgumentCaptor.getValue();

        assertEquals(expectedRestaurantEntity, savedRestaurantEntity);
        assertEquals(expectedRestaurantEntity.getRestaurantOwner(), savedRestaurantEntity.getRestaurantOwner());

    }

    @Test
    void assignFoodMenuToRestaurant_shouldWorkSuccessfully() {
        //  given
        RestaurantEntity expectedRestaurantEntity = someRestaurantEntity4();
        FoodMenuEntity menuEntity = someFoodMenuEntity2();
        when(restaurantJpaRepository.findByEmail(expectedRestaurantEntity.getEmail())).thenReturn(Optional.of(expectedRestaurantEntity));

        //  when
        restaurantRepository.assignFoodMenuToRestaurant(expectedRestaurantEntity.getEmail(), menuEntity);

        //  then
        ArgumentCaptor<RestaurantEntity> argumentCaptor = ArgumentCaptor.forClass(RestaurantEntity.class);
        verify(restaurantJpaRepository).save(argumentCaptor.capture());

        RestaurantEntity savedRestaurantEntityWithFoodMenu = argumentCaptor.getValue();

        assertEquals(expectedRestaurantEntity, savedRestaurantEntityWithFoodMenu);
        assertEquals(expectedRestaurantEntity.getFoodMenu(), savedRestaurantEntityWithFoodMenu.getFoodMenu());

    }

    @Test
    void assignFoodMenuToRestaurant_shouldThrowException() {
        //  given
        FoodMenuEntity menuEntity = someFoodMenuEntity2();
        String email = "na_wypasie@restaurant.pl";
        when(restaurantJpaRepository.findByEmail(email)).thenReturn(Optional.empty());

        //  when
        //  then
        assertThatThrownBy(() -> restaurantRepository.assignFoodMenuToRestaurant(email, menuEntity))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Restaurant with email: [%s] not found".formatted(email));
    }

    @Test
    void assignFoodToFoodMenuInRestaurant_shouldWorkSuccessfully() {
        //  given
        RestaurantEntity expectedRestaurantEntity = someRestaurantEntity5();
        FoodEntity foodEntity = someFoodEntity4();
        when(restaurantJpaRepository.findByEmail(expectedRestaurantEntity.getEmail())).thenReturn(Optional.of(expectedRestaurantEntity));

        //  when
        restaurantRepository.assignFoodToFoodMenuInRestaurant(expectedRestaurantEntity.getEmail(), foodEntity);

        //  then
        ArgumentCaptor<RestaurantEntity> argumentCaptor = ArgumentCaptor.forClass(RestaurantEntity.class);
        verify(restaurantJpaRepository).save(argumentCaptor.capture());

        RestaurantEntity savedRestaurantEntity = argumentCaptor.getValue();

        assertEquals(expectedRestaurantEntity, savedRestaurantEntity);
        assertEquals(expectedRestaurantEntity.getFoodMenu().getFoods().get(2), savedRestaurantEntity.getFoodMenu().getFoods().get(2));

    }

    @Test
    void assignFoodToFoodMenuInRestaurant_shouldThrowException() {
        //  given
        FoodEntity foodEntity = someFoodEntity1();
        String email = "zapiecek@restaurant.pl";
        when(restaurantJpaRepository.findByEmail(email)).thenReturn(Optional.empty());

        //  when
        //  then
        assertThatThrownBy(() -> restaurantRepository.assignFoodToFoodMenuInRestaurant(email, foodEntity))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Restaurant with email: [%s] not found".formatted(email));

    }
}
