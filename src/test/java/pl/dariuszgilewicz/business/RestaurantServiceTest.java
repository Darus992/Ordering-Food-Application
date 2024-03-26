package pl.dariuszgilewicz.business;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.dariuszgilewicz.infrastructure.database.entity.AddressEntity;
import pl.dariuszgilewicz.infrastructure.database.entity.FoodMenuEntity;
import pl.dariuszgilewicz.infrastructure.database.entity.RestaurantEntity;
import pl.dariuszgilewicz.infrastructure.database.repository.RestaurantRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.jpa.AddressJpaRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.jpa.FoodMenuJpaRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.jpa.RestaurantJpaRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.mapper.RestaurantEntityMapper;
import pl.dariuszgilewicz.infrastructure.model.Restaurant;
import pl.dariuszgilewicz.infrastructure.request_form.RestaurantRequestForm;
import pl.dariuszgilewicz.infrastructure.security.User;
import pl.dariuszgilewicz.infrastructure.security.UserService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static pl.dariuszgilewicz.util.AddressFixtures.someListOfAddressEntity1;
import static pl.dariuszgilewicz.util.FoodMenuFixtures.someListOfFoodMenuEntities1;
import static pl.dariuszgilewicz.util.RestaurantFixtures.*;
import static pl.dariuszgilewicz.util.RestaurantRequestFormFixtures.someRestaurantRequestForm1;
import static pl.dariuszgilewicz.util.UsersFixtures.someMappedBusinessUser1;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {

    @InjectMocks
    private RestaurantService restaurantService;

    @Mock
    private RestaurantJpaRepository restaurantJpaRepository;

    @Mock
    private RestaurantEntityMapper restaurantEntityMapper;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private FoodMenuJpaRepository foodMenuJpaRepository;

    @Mock
    private AddressJpaRepository addressJpaRepository;

    @Mock
    private UserService userService;


    @Test
    @DisplayName("Find restaurant by restaurant email work successfully")
    void findRestaurantByEmail_shouldWorkCorrectly() {
        //  given
        String email = "na_wypasie@restaurant.pl";
        RestaurantEntity restaurantEntity = someRestaurantEntity1();
        Restaurant expectedRestaurant = someRestaurantModel1();
        when(restaurantJpaRepository.findByEmail(email)).thenReturn(Optional.of(restaurantEntity));
        when(restaurantEntityMapper.mapFromEntity(restaurantEntity)).thenReturn(expectedRestaurant);


        //  when
        Restaurant resultRestaurant = restaurantService.findRestaurantByEmail(email);

        //  then
        assertEquals(expectedRestaurant.getRestaurantName(), resultRestaurant.getRestaurantName());
        assertEquals(expectedRestaurant.getRestaurantPhone(), resultRestaurant.getRestaurantPhone());
        assertEquals(expectedRestaurant.getRestaurantEmail(), resultRestaurant.getRestaurantEmail());
        assertEquals(expectedRestaurant.getRestaurantAddress(), resultRestaurant.getRestaurantAddress());
        assertEquals(expectedRestaurant.getRestaurantOpeningTime(), resultRestaurant.getRestaurantOpeningTime());

        verify(restaurantJpaRepository).findByEmail(email);
        verify(restaurantEntityMapper).mapFromEntity(restaurantEntity);

    }

    @Test
    @DisplayName("Finding restaurant by restaurant email should throw exception")
    void findRestaurantByEmail_shouldThrowException() {
        //  given
        String email = "notExistingRestaurantEmail@restaurant.com";
        when(restaurantJpaRepository.findByEmail(email)).thenReturn(Optional.empty());

        //  when
        //  then
        assertThrows(EntityNotFoundException.class, () -> restaurantService.findRestaurantByEmail(email));

    }

    @Test
    void findAllRestaurantsWithSelectedCategory_shouldThrowException() {
        //  given
        String categoryName = "Pizza";
        when(foodMenuJpaRepository.findAllByCategory(categoryName)).thenReturn(Optional.empty());

        //  when
        //  then
        assertThatThrownBy(() -> restaurantService.findAllRestaurantsWithSelectedCategory(categoryName))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Not found List of FoodMenuEntity by category name: [%s]".formatted(categoryName));

    }

    @Test
    @DisplayName("Return list of the restaurants depends on the selected category work successfully")
    void findAllRestaurantsWithSelectedCategory_shouldWorkSuccessfully() {
        //  given
        String categoryName = "Pizza";
        List<FoodMenuEntity> foodMenuEntityList = someListOfFoodMenuEntities1();
        List<Restaurant> expectedList = someListOfMappedRestaurants1();
        when(foodMenuJpaRepository.findAllByCategory(categoryName)).thenReturn(Optional.of(foodMenuEntityList));
        when(restaurantRepository.findAllRestaurantsWithSelectedCategory(foodMenuEntityList)).thenReturn(expectedList);

        //  when
        List<Restaurant> resultList = restaurantService.findAllRestaurantsWithSelectedCategory(categoryName);

        //  then
        assertEquals(expectedList, resultList);

    }

    @Test
    void findRestaurantsNearYouByAddress_shouldThrowException() {
        //  given
        String searchingTerm = "Warszawa";
        when(addressJpaRepository.findBySearchTerm(searchingTerm)).thenReturn(Optional.empty());

        //  when
        //  then
        assertThatThrownBy(() -> restaurantService.findRestaurantsNearYouByAddress(searchingTerm))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Not found List of AddressEntity by searchTerm: [%s]".formatted(searchingTerm));

    }

    @Test
    void findRestaurantsNearYouByAddress_shouldWorkSuccessfully() {
        //  given
        String searchingTerm = "Warszawa";
        List<AddressEntity> addressEntities = someListOfAddressEntity1();
        List<Restaurant> expectedList = someListOfMappedRestaurants1();
        when(addressJpaRepository.findBySearchTerm(searchingTerm)).thenReturn(Optional.of(addressEntities));
        when(restaurantRepository.findRestaurantsNearYouByAddress(addressEntities)).thenReturn(expectedList);

        //  when
        List<Restaurant> resultList = restaurantService.findRestaurantsNearYouByAddress(searchingTerm);

        //  then
        assertEquals(expectedList, resultList);

    }

    @Test
    void createRestaurantAndAssignToOwner_shouldWorkSuccessfully() {
        //  given
        User businessUser = someMappedBusinessUser1();
        RestaurantRequestForm requestForm = someRestaurantRequestForm1();
        given(userService.getCurrentUser()).willReturn(businessUser);

        //  when
        restaurantService.createRestaurantAndAssignToOwner(requestForm);

        //  then
        then(restaurantRepository).should().createRestaurantFromRestaurantRequest(requestForm, businessUser.getRestaurantOwner().getPesel());

    }
}