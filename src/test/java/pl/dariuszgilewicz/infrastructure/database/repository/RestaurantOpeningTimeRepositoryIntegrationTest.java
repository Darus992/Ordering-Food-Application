package pl.dariuszgilewicz.infrastructure.database.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import pl.dariuszgilewicz.configuration.AbstractJpaIT;
import pl.dariuszgilewicz.infrastructure.database.entity.RestaurantOpeningTimeEntity;
import pl.dariuszgilewicz.infrastructure.database.repository.jpa.RestaurantOpeningTimeJpaRepository;
import pl.dariuszgilewicz.util.RestaurantOpeningTimeFixtures;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static pl.dariuszgilewicz.util.RestaurantOpeningTimeFixtures.*;

@Import(RestaurantOpeningTimeRepository.class)
class RestaurantOpeningTimeRepositoryIntegrationTest extends AbstractJpaIT {

    @Autowired
    private RestaurantOpeningTimeJpaRepository jpaRepository;
    @Autowired
    private RestaurantOpeningTimeRepository restaurantOpeningTimeRepository;

    @Test
    void saveAndReturn_shouldWorkSuccessfully() {
        //  given
        RestaurantOpeningTimeEntity openingTimeEntity = someRestaurantOpeningTimeEntity1();

        //  when
        RestaurantOpeningTimeEntity returnedEntity = restaurantOpeningTimeRepository.saveAndReturn(openingTimeEntity);

        //  then
        assertNotNull(returnedEntity);

        //  verify persistence
        RestaurantOpeningTimeEntity persistenceEntity = jpaRepository.findById(returnedEntity.getRestaurantOpeningTimeId()).orElseThrow();
        List<RestaurantOpeningTimeEntity> all = jpaRepository.findAll();

        assertNotNull(persistenceEntity);
        assertEquals(returnedEntity, persistenceEntity);
        assertEquals(1, all.size());
    }

    @Test
    void saveAndReturn_shouldThrowExceptionWhenEntityIsNullValue() {
        //  given
        //  when
        //  then
        assertThrows(InvalidDataAccessApiUsageException.class, () -> restaurantOpeningTimeRepository.saveAndReturn(null));

        List<RestaurantOpeningTimeEntity> all = jpaRepository.findAll();

        assertEquals(0, all.size());
    }
}