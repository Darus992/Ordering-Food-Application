package pl.dariuszgilewicz.infrastructure.database.repository.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.dariuszgilewicz.infrastructure.database.entity.RestaurantOpeningTimeEntity;
import pl.dariuszgilewicz.infrastructure.model.RestaurantOpeningTime;
import pl.dariuszgilewicz.infrastructure.request_form.BusinessRequestForm;
import pl.dariuszgilewicz.infrastructure.request_form.RestaurantRequestForm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.dariuszgilewicz.util.BusinessRequestFormFixtures.someBusinessRequestForm1;
import static pl.dariuszgilewicz.util.RestaurantOpeningTimeFixtures.*;
import static pl.dariuszgilewicz.util.RestaurantRequestFormFixtures.someRestaurantRequestForm1;

@ExtendWith(MockitoExtension.class)
class RestaurantOpeningTimeEntityMapperTest {

    @InjectMocks
    private RestaurantOpeningTimeEntityMapper restaurantOpeningTimeEntityMapper;


    @Test
    void mapFromEntity_shouldWorkSuccessfully() {
        //  given
        RestaurantOpeningTimeEntity openingTimeEntity = someRestaurantOpeningTimeEntity1();
        RestaurantOpeningTime expectedOpeningTime = someRestaurantOpeningTimeModel1();

        //  when
        RestaurantOpeningTime resultOpeningTime = restaurantOpeningTimeEntityMapper.mapFromEntity(openingTimeEntity);

        //  then
        assertEquals(expectedOpeningTime, resultOpeningTime);
    }

    @Test
    void mapToEntity_shouldWorkSuccessfully() {
        //  given
        RestaurantOpeningTimeEntity expectedOpeningTimeEntity = someRestaurantOpeningTimeEntity1();
        RestaurantOpeningTime openingTime = someRestaurantOpeningTimeModel1();

        //  when
        RestaurantOpeningTimeEntity resultOpeningTimeEntity = restaurantOpeningTimeEntityMapper.mapToEntity(openingTime);

        //  then
        assertEquals(expectedOpeningTimeEntity, resultOpeningTimeEntity);
    }

    @Test
    void mapFromBusinessRequest_shouldWorkSuccessfully() {
        //  given
        RestaurantOpeningTimeEntity expectedOpeningTimeEntity = someRestaurantOpeningTimeEntity1();
        BusinessRequestForm requestForm = someBusinessRequestForm1();

        //  when
        RestaurantOpeningTimeEntity resultOpeningTimeEntity = restaurantOpeningTimeEntityMapper.mapFromBusinessRequest(requestForm);

        //  then
        assertEquals(expectedOpeningTimeEntity, resultOpeningTimeEntity);
    }

    @Test
    void mapFromRestaurantRequest_shouldWorkSuccessfully() {
        RestaurantOpeningTimeEntity expectedOpeningTimeEntity = someRestaurantOpeningTimeEntity2();
        RestaurantRequestForm requestForm = someRestaurantRequestForm1();

        //  when
        RestaurantOpeningTimeEntity resultOpeningTimeEntity = restaurantOpeningTimeEntityMapper.mapFromRestaurantRequest(requestForm);

        //  then
        assertEquals(expectedOpeningTimeEntity, resultOpeningTimeEntity);
    }
}