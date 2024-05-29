package pl.dariuszgilewicz.infrastructure.database.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import pl.dariuszgilewicz.configuration.AbstractSpringBootIT;
import pl.dariuszgilewicz.infrastructure.database.entity.FoodEntity;
import pl.dariuszgilewicz.infrastructure.database.entity.FoodMenuEntity;
import pl.dariuszgilewicz.infrastructure.database.repository.jpa.FoodMenuJpaRepository;
import pl.dariuszgilewicz.infrastructure.model.FoodMenu;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static pl.dariuszgilewicz.util.FoodMenuFixtures.someFoodMenuModel4;

class FoodMenuRepositoryIntegrationTest extends AbstractSpringBootIT {

    @Autowired
    private FoodMenuRepository foodMenuRepository;
    @Autowired
    private FoodMenuJpaRepository jpaRepository;

    @BeforeEach
    void setUp() {
        jpaRepository.deleteAll();
    }

    @Test
    void createAndReturnFoodMenuEntity_shouldWorkSuccessfully() {
        //  given
        FoodMenu foodMenu = someFoodMenuModel4();

        //  when
        FoodMenuEntity resultMenuEntity = foodMenuRepository.createAndReturnFoodMenuEntity(foodMenu);


        //  then
        //  verify persistence
        FoodMenuEntity persistenceEntity = jpaRepository.findById(resultMenuEntity.getFoodMenuId()).orElseThrow();
        List<FoodMenuEntity> all = jpaRepository.findAll();

        FoodEntity persistenceFoodEntity = persistenceEntity.getFoods().get(0);
        FoodEntity resultFoodEntity = resultMenuEntity.getFoods().get(0);

        assertNotNull(resultMenuEntity);
        assertNotNull(persistenceEntity);
        assertThat(persistenceFoodEntity.getPrice()).isEqualByComparingTo(resultFoodEntity.getPrice());

        assertEquals(resultMenuEntity.getFoodMenuId(), persistenceEntity.getFoodMenuId());
        assertEquals(resultMenuEntity.getMenuName(), persistenceEntity.getMenuName());
        assertEquals(persistenceFoodEntity.getCategory(), resultFoodEntity.getCategory());
        assertEquals(persistenceFoodEntity.getName(), resultFoodEntity.getName());
        assertEquals(persistenceFoodEntity.getDescription(), resultFoodEntity.getDescription());
        assertArrayEquals(persistenceFoodEntity.getFoodImage(), resultFoodEntity.getFoodImage());
        assertEquals(1, all.size());
    }

    @Test
    void createAndReturnFoodMenuEntity_shouldThrowExceptionWhenFileIsNullValue() {
        //  given
        FoodMenu foodMenu = someFoodMenuModel4();
        foodMenu.setFoodMenuName(null);

        //  when
        //  then
        assertThrows(DataIntegrityViolationException.class, () -> foodMenuRepository.createAndReturnFoodMenuEntity(foodMenu));

        //  verify persistence
        List<FoodMenuEntity> all = jpaRepository.findAll();
        assertEquals(0, all.size());
    }
}