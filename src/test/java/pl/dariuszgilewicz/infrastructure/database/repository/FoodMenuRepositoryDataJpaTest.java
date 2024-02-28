package pl.dariuszgilewicz.infrastructure.database.repository;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import pl.dariuszgilewicz.configuration.AbstractJpaIT;
import pl.dariuszgilewicz.infrastructure.database.entity.FoodMenuEntity;
import pl.dariuszgilewicz.infrastructure.database.repository.jpa.FoodMenuJpaRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.mapper.FoodMenuEntityMapper;
import pl.dariuszgilewicz.infrastructure.model.FoodMenu;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static pl.dariuszgilewicz.util.FoodMenuFixtures.someFoodMenu2;
import static pl.dariuszgilewicz.util.FoodMenuFixtures.someFoodMenuEntity2;

@AllArgsConstructor(onConstructor = @__(@Autowired))
class FoodMenuRepositoryDataJpaTest extends AbstractJpaIT {

    private FoodMenuJpaRepository foodMenuJpaRepository;

    @MockBean
    private FoodMenuEntityMapper foodMenuEntityMapper;

    @Test
    void createAndReturnFoodMenuEntity_shouldWorkCorrectly() {
        //  given
        FoodMenu foodMenu = someFoodMenu2();
        FoodMenuEntity foodMenuEntity = someFoodMenuEntity2();
        when(foodMenuEntityMapper.mapToEntity(foodMenu)).thenReturn(foodMenuEntity);

        //  when
        foodMenuJpaRepository.save(foodMenuEntity);
        List<FoodMenuEntity> results = foodMenuJpaRepository.findAllByCategory(foodMenuEntity.getFoods().get(0).getCategory()).get();

        //  then
        assertNotNull(results.get(0));
        assertEquals(foodMenuEntity, results.get(0));
    }
}