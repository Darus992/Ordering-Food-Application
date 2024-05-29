package pl.dariuszgilewicz.infrastructure.database.repository;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import pl.dariuszgilewicz.configuration.AbstractJpaIT;
import pl.dariuszgilewicz.infrastructure.database.entity.FoodEntity;
import pl.dariuszgilewicz.infrastructure.database.repository.jpa.FoodJpaRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static pl.dariuszgilewicz.util.FoodFixtures.someFoodEntity1;

@Import(FoodRepository.class)
class FoodRepositoryIntegrationTest extends AbstractJpaIT {

    @Autowired
    private FoodJpaRepository jpaRepository;
    @Autowired
    private FoodRepository foodRepository;

    @Test
    void saveAndReturn_shouldWorkSuccessfully() {
        //  given
        FoodEntity foodEntity = someFoodEntity1();
        foodEntity.setFoodId(null);

        //  when
        FoodEntity returnedEntity = foodRepository.saveAndReturn(foodEntity);

        //  then
        assertNotNull(returnedEntity);
        assertEquals(foodEntity.getCategory(), returnedEntity.getCategory());
        assertEquals(foodEntity.getName(), returnedEntity.getName());
        assertEquals(foodEntity.getDescription(), returnedEntity.getDescription());
        assertEquals(foodEntity.getPrice(), returnedEntity.getPrice());

        //  verify persistence
        FoodEntity persistenceEntity = jpaRepository.findById(returnedEntity.getFoodId()).orElseThrow();
        List<FoodEntity> all = jpaRepository.findAll();

        assertNotNull(persistenceEntity);
        assertEquals(returnedEntity, persistenceEntity);
        assertEquals(1, all.size());
    }

    @Test
    void saveAndReturn_shouldThrowExceptionWhenEntityIsNullValue() {
        //  given
        //  when
        //  then
        assertThrows(InvalidDataAccessApiUsageException.class, () -> foodRepository.saveAndReturn(null));

        //  verify persistence
        List<FoodEntity> all = jpaRepository.findAll();
        assertEquals(0, all.size());
    }

    @Test
    void findFoodEntityById_shouldWorkSuccessfully() {
        //  given
        FoodEntity expectedEntity = someFoodEntity1();
        expectedEntity.setFoodId(1);
        int foodId = 1;

        jpaRepository.save(expectedEntity);

        //  when
        FoodEntity resultEntity = foodRepository.findFoodEntityById(foodId);

        //  then
        assertNotNull(resultEntity);
        assertEquals(expectedEntity, resultEntity);

        //  verify persistence
        FoodEntity persistenceEntity = jpaRepository.findById(foodId).orElseThrow(() -> new EntityNotFoundException(
                "FoodEntity with id: [%s] not found".formatted(foodId)
        ));
        List<FoodEntity> all = jpaRepository.findAll();

        assertNotNull(persistenceEntity);
        assertEquals(resultEntity, persistenceEntity);
        assertEquals(1, all.size());
    }

    @Test
    void findFoodEntityById_shouldThrowExceptionWhenFoodIdIsNotFound() {
        //  given
        int nonExistedFoodId = 1;

        //  when
        //  then
        assertThrows(EntityNotFoundException.class, () -> foodRepository.findFoodEntityById(nonExistedFoodId));

        //  verify persistence
        List<FoodEntity> all = jpaRepository.findAll();

        assertEquals(0, all.size());
    }
}