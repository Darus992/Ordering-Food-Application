package pl.dariuszgilewicz.infrastructure.database.repository.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.dariuszgilewicz.infrastructure.database.entity.FoodMenuEntity;
import pl.dariuszgilewicz.infrastructure.model.FoodMenu;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static pl.dariuszgilewicz.util.FoodMenuFixtures.someFoodMenuEntity1;
import static pl.dariuszgilewicz.util.FoodMenuFixtures.someFoodMenuModel1;

@ExtendWith(MockitoExtension.class)
class FoodMenuEntityMapperTest {

    @InjectMocks
    private FoodMenuEntityMapper foodMenuEntityMapper;

    @Mock
    private FoodEntityMapper foodEntityMapper;

    @Test
    void mapFromEntity_shouldWorkSuccessfully() {
        //  given
        FoodMenuEntity foodMenuEntity = someFoodMenuEntity1();
        FoodMenu expectedFoodMenu = someFoodMenuModel1();

        when(foodEntityMapper.mapFromEntityList(foodMenuEntity.getFoods())).thenReturn(expectedFoodMenu.getFoods());

        //  when
        FoodMenu resultFoodMenu = foodMenuEntityMapper.mapFromEntity(foodMenuEntity);

        //  then
        assertEquals(expectedFoodMenu, resultFoodMenu);
    }

    @Test
    void mapToEntity_shouldWorkSuccessfully() {
        //  given
        FoodMenuEntity expectedFoodMenuEntity = someFoodMenuEntity1();
        FoodMenu foodMenu = someFoodMenuModel1();

        when(foodEntityMapper.mapToEntityList(foodMenu.getFoods())).thenReturn(expectedFoodMenuEntity.getFoods());

        //  when
        FoodMenuEntity resultFoodMenuEntity = foodMenuEntityMapper.mapToEntity(foodMenu);

        //  then
        assertEquals(expectedFoodMenuEntity, resultFoodMenuEntity);
    }
}