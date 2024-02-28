package pl.dariuszgilewicz.infrastructure.database.repository;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import pl.dariuszgilewicz.configuration.AbstractJpaIT;
import pl.dariuszgilewicz.infrastructure.database.entity.RestaurantOwnerEntity;
import pl.dariuszgilewicz.infrastructure.database.repository.jpa.RestaurantOwnerJpaRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.mapper.RestaurantOwnerEntityMapper;
import pl.dariuszgilewicz.infrastructure.model.RestaurantOwner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static pl.dariuszgilewicz.util.RestaurantOwnerFixtures.someRestaurantOwner1;
import static pl.dariuszgilewicz.util.RestaurantOwnerFixtures.someRestaurantOwnerEntity1;

@AllArgsConstructor(onConstructor = @__(@Autowired))
class RestaurantOwnerRepositoryDataJpaTest extends AbstractJpaIT {

    private RestaurantOwnerJpaRepository restaurantOwnerJpaRepository;

    @MockBean
    private RestaurantOwnerEntityMapper restaurantOwnerEntityMapper;

    @Test
    void createRestaurantOwner_shouldWorkCorrectly() {
        //  given
        RestaurantOwner restaurantOwner = someRestaurantOwner1();
        RestaurantOwnerEntity restaurantOwnerEntity = someRestaurantOwnerEntity1();
        when(restaurantOwnerEntityMapper.mapToEntity(restaurantOwner)).thenReturn(restaurantOwnerEntity);

        //  when
        restaurantOwnerJpaRepository.save(restaurantOwnerEntity);
        RestaurantOwnerEntity result = restaurantOwnerJpaRepository.findByPesel(restaurantOwner.getPesel()).get();

        //  then
        assertNotNull(result);
        assertEquals(restaurantOwnerEntity, result);
    }
}