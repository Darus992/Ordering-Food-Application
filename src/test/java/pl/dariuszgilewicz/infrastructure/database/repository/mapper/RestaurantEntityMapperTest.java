package pl.dariuszgilewicz.infrastructure.database.repository.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.dariuszgilewicz.infrastructure.database.entity.FoodMenuEntity;
import pl.dariuszgilewicz.infrastructure.database.entity.RestaurantEntity;
import pl.dariuszgilewicz.infrastructure.database.entity.RestaurantOwnerEntity;
import pl.dariuszgilewicz.infrastructure.model.Restaurant;
import pl.dariuszgilewicz.infrastructure.request_form.BusinessRequestForm;
import pl.dariuszgilewicz.infrastructure.request_form.RestaurantRequestForm;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static pl.dariuszgilewicz.util.BusinessRequestFormFixtures.someBusinessRequestForm3;
import static pl.dariuszgilewicz.util.RestaurantFixtures.*;
import static pl.dariuszgilewicz.util.RestaurantOwnerFixtures.someRestaurantOwnerEntity1;
import static pl.dariuszgilewicz.util.RestaurantRequestFormFixtures.someRestaurantRequestForm2;

@ExtendWith(MockitoExtension.class)
class RestaurantEntityMapperTest {

    @InjectMocks
    private RestaurantEntityMapper restaurantEntityMapper;

    @Mock
    private FoodMenuEntityMapper foodMenuEntityMapper;
    @Mock
    private AddressEntityMapper addressEntityMapper;
    @Mock
    private RestaurantOpeningTimeEntityMapper restaurantOpeningTimeEntityMapper;


    @Test
    void mapFromEntity_shouldWorkSuccessfully() {
        //  given
        RestaurantEntity restaurantEntity = someRestaurantEntity5();
        Restaurant expectedRestaurant = someRestaurantModel5();

        restaurantEntity.setCustomerOrders(List.of());

        when(foodMenuEntityMapper.mapFromEntity(restaurantEntity.getFoodMenu())).thenReturn(expectedRestaurant.getFoodMenu());
        when(addressEntityMapper.mapFromEntity(restaurantEntity.getRestaurantAddress())).thenReturn(expectedRestaurant.getRestaurantAddress());
        when(restaurantOpeningTimeEntityMapper.mapFromEntity(restaurantEntity.getRestaurantOpeningTime())).thenReturn(expectedRestaurant.getRestaurantOpeningTime());

        //  when
        Restaurant resultRestaurant = restaurantEntityMapper.mapFromEntity(restaurantEntity);

        //  then
        assertEquals(expectedRestaurant, resultRestaurant);
    }

    @Test
    void mapToEntity_shouldWorkSuccessfully() {
        //  given
        Restaurant restaurant = someRestaurantModel5();
        RestaurantEntity expectedRestaurantEntity = someRestaurantEntity5();

        when(foodMenuEntityMapper.mapToEntity(restaurant.getFoodMenu())).thenReturn(expectedRestaurantEntity.getFoodMenu());
        when(addressEntityMapper.mapToEntity(restaurant.getRestaurantAddress())).thenReturn(expectedRestaurantEntity.getRestaurantAddress());
        when(restaurantOpeningTimeEntityMapper.mapToEntity(restaurant.getRestaurantOpeningTime())).thenReturn(expectedRestaurantEntity.getRestaurantOpeningTime());

        //  when
        RestaurantEntity resultRestaurantEntity = restaurantEntityMapper.mapToEntity(restaurant);

        //  then
        assertEquals(expectedRestaurantEntity, resultRestaurantEntity);
    }

    @Test
    void mapFromBusinessRequest_shouldWorkSuccessfully() throws IOException {
        //  given
        BusinessRequestForm requestForm = someBusinessRequestForm3();
        RestaurantEntity expectedRestaurantEntity = someRestaurantEntity5();
        expectedRestaurantEntity.setFoodMenu(new FoodMenuEntity(null, requestForm.getRestaurantName() + " Menu", null));

        when(addressEntityMapper.mapFromBusinessRequest(requestForm)).thenReturn(expectedRestaurantEntity.getRestaurantAddress());
        when(restaurantOpeningTimeEntityMapper.mapFromBusinessRequest(requestForm)).thenReturn(expectedRestaurantEntity.getRestaurantOpeningTime());

        //  when
        RestaurantEntity resultRestaurantEntity = restaurantEntityMapper.mapFromBusinessRequest(requestForm);

        //  then
        assertEquals(expectedRestaurantEntity, resultRestaurantEntity);
    }

    @Test
    void mapFromRestaurantRequest_shouldWorkSuccessfully() {
        //  given
        RestaurantRequestForm requestForm = someRestaurantRequestForm2();
        RestaurantOwnerEntity ownerEntity = someRestaurantOwnerEntity1();
        RestaurantEntity expectedRestaurantEntity = someRestaurantEntity5();
        expectedRestaurantEntity.setFoodMenu(new FoodMenuEntity(null, requestForm.getRestaurantName() + " Menu", null));
        expectedRestaurantEntity.setRestaurantOwner(ownerEntity);

        when(addressEntityMapper.mapFromRestaurantRequest(requestForm)).thenReturn(expectedRestaurantEntity.getRestaurantAddress());
        when(restaurantOpeningTimeEntityMapper.mapFromRestaurantRequest(requestForm)).thenReturn(expectedRestaurantEntity.getRestaurantOpeningTime());

        //  when
        RestaurantEntity resultRestaurantEntity = restaurantEntityMapper.mapFromRestaurantRequest(requestForm, ownerEntity);

        //  then
        assertEquals(expectedRestaurantEntity, resultRestaurantEntity);
    }

    @Test
    void mapFromEntityList_shouldWorkSuccessfully() {
        //  given
        List<Restaurant> expectedRestaurants = someListOfMappedRestaurants3();
        List<RestaurantEntity> restaurantEntities = someListOfRestaurantEntities3();

        for (int i = 0; i < expectedRestaurants.size(); i++) {
            when(foodMenuEntityMapper.mapFromEntity(restaurantEntities.get(i).getFoodMenu())).thenReturn(expectedRestaurants.get(i).getFoodMenu());
            when(addressEntityMapper.mapFromEntity(restaurantEntities.get(i).getRestaurantAddress())).thenReturn(expectedRestaurants.get(i).getRestaurantAddress());
            when(restaurantOpeningTimeEntityMapper.mapFromEntity(restaurantEntities.get(i).getRestaurantOpeningTime())).thenReturn(expectedRestaurants.get(i).getRestaurantOpeningTime());

            restaurantEntities.get(i).setCustomerOrders(List.of());
        }

        //  when
        List<Restaurant> resultRestaurants = restaurantEntityMapper.mapFromEntityList(restaurantEntities);

        //  then
        assertEquals(expectedRestaurants, resultRestaurants);
    }

    @Test
    void mapToEntityList_shouldWorkSuccessfully() {
        //  given
        List<Restaurant> restaurants = someListOfMappedRestaurants3();
        List<RestaurantEntity> expectedRestaurantEntities = someListOfRestaurantEntities3();

        for (int i = 0; i < restaurants.size(); i++) {
            when(foodMenuEntityMapper.mapToEntity(restaurants.get(i).getFoodMenu())).thenReturn(expectedRestaurantEntities.get(i).getFoodMenu());
            when(addressEntityMapper.mapToEntity(restaurants.get(i).getRestaurantAddress())).thenReturn(expectedRestaurantEntities.get(i).getRestaurantAddress());
            when(restaurantOpeningTimeEntityMapper.mapToEntity(restaurants.get(i).getRestaurantOpeningTime())).thenReturn(expectedRestaurantEntities.get(i).getRestaurantOpeningTime());
        }

        //  when
        List<RestaurantEntity> resultRestaurantEntities = restaurantEntityMapper.mapToEntityList(restaurants);

        //  then
        assertEquals(expectedRestaurantEntities, resultRestaurantEntities);
    }
}