package pl.dariuszgilewicz.infrastructure.database.repository.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.dariuszgilewicz.infrastructure.database.entity.RestaurantOwnerEntity;
import pl.dariuszgilewicz.infrastructure.model.RestaurantOwner;
import pl.dariuszgilewicz.infrastructure.request_form.BusinessRequestForm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static pl.dariuszgilewicz.util.BusinessRequestFormFixtures.someBusinessRequestForm1;
import static pl.dariuszgilewicz.util.RestaurantOwnerFixtures.someRestaurantOwnerEntity1;
import static pl.dariuszgilewicz.util.RestaurantOwnerFixtures.someRestaurantOwnerModel1;

@ExtendWith(MockitoExtension.class)
class RestaurantOwnerEntityMapperTest {

    @InjectMocks
    private RestaurantOwnerEntityMapper restaurantOwnerEntityMapper;
    @Mock
    private RestaurantEntityMapper restaurantEntityMapper;


    @Test
    void mapFromEntity_shouldWorkSuccessfully() {
        //  given
        RestaurantOwnerEntity ownerEntity = someRestaurantOwnerEntity1();
        RestaurantOwner expectedOwner = someRestaurantOwnerModel1();

        when(restaurantEntityMapper.mapFromEntityList(ownerEntity.getRestaurants())).thenReturn(expectedOwner.getRestaurants());

        //  when
        RestaurantOwner resultOwner = restaurantOwnerEntityMapper.mapFromEntity(ownerEntity);

        //  then
        assertEquals(expectedOwner, resultOwner);
    }

    @Test
    void mapToEntity_shouldWorkSuccessfully() {
        //  given
        RestaurantOwnerEntity expectedOwnerEntity = someRestaurantOwnerEntity1();
        RestaurantOwner owner = someRestaurantOwnerModel1();

        when(restaurantEntityMapper.mapToEntityList(owner.getRestaurants())).thenReturn(expectedOwnerEntity.getRestaurants());

        //  when
        RestaurantOwnerEntity resultOwnerEntity = restaurantOwnerEntityMapper.mapToEntity(owner);

        //  then
        assertEquals(expectedOwnerEntity, resultOwnerEntity);
    }

    @Test
    void mapFromBusinessRequest_shouldWorkSuccessfully() {
        //  given
        BusinessRequestForm requestForm = someBusinessRequestForm1();
        RestaurantOwnerEntity expectedOwnerEntity = someRestaurantOwnerEntity1();

        //  when
        RestaurantOwnerEntity resultOwnerEntity = restaurantOwnerEntityMapper.mapFromBusinessRequest(requestForm);

        //  then
        assertEquals(expectedOwnerEntity, resultOwnerEntity);
    }
}