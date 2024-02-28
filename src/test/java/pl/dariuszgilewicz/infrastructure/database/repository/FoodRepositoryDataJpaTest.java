package pl.dariuszgilewicz.infrastructure.database.repository;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import pl.dariuszgilewicz.configuration.AbstractJpaIT;
import pl.dariuszgilewicz.infrastructure.database.entity.FoodEntity;
import pl.dariuszgilewicz.infrastructure.database.repository.jpa.FoodJpaRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.mapper.FoodEntityMapper;
import pl.dariuszgilewicz.infrastructure.model.Food;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static pl.dariuszgilewicz.util.FoodFixtures.someFood1;
import static pl.dariuszgilewicz.util.FoodFixtures.someFoodEntity1;

@AllArgsConstructor(onConstructor = @__(@Autowired))
class FoodRepositoryDataJpaTest extends AbstractJpaIT {

    private FoodJpaRepository foodJpaRepository;

    @MockBean
    private FoodEntityMapper foodEntityMapper;

    @Test
    void createAndReturnFoodEntity_shouldWorkCorrectly() {
        //  given
        Food food = someFood1();
        FoodEntity foodEntity = someFoodEntity1();
        when(foodEntityMapper.mapToEntity(food)).thenReturn(foodEntity);

        //  when
        foodJpaRepository.save(foodEntity);
        List<FoodEntity> results = foodJpaRepository.findAll();

        //  then
        assertNotNull(results.get(0));
        assertEquals(foodEntity, results.get(0));
    }
}