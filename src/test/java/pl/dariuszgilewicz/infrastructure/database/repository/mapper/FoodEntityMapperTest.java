package pl.dariuszgilewicz.infrastructure.database.repository.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.dariuszgilewicz.infrastructure.database.entity.FoodEntity;
import pl.dariuszgilewicz.infrastructure.model.Food;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.dariuszgilewicz.util.FoodFixtures.*;

@ExtendWith(MockitoExtension.class)
class FoodEntityMapperTest {

    @InjectMocks
    private FoodEntityMapper foodEntityMapper;

    @Test
    void mapFromEntity_shouldWorkSuccessfully() {
        //  given
        FoodEntity foodEntity = someFoodEntity2();
        foodEntity.setFoodImage(new byte[0]);

        Food expectedFood = someFoodModel2();
        expectedFood.setFoodImage("");

        //  when
        Food resultFood = foodEntityMapper.mapFromEntity(foodEntity);

        //  then
        assertEquals(expectedFood, resultFood);
    }

    @Test
    void mapToEntity_shouldWorkSuccessfully() {
        //  given
        FoodEntity expectedFoodEntity = someFoodEntity2();
        expectedFoodEntity.setFoodImage(new byte[0]);

        Food food = someFoodModel2();
        food.setFoodImage("");

        //  when
        FoodEntity resultFoodEntity = foodEntityMapper.mapToEntity(food);

        //  then
        assertEquals(expectedFoodEntity, resultFoodEntity);
    }

    @Test
    void mapFromEntityList_shouldWorkSuccessfully() {
        //  given
        List<FoodEntity> foodEntities = someFoodEntityList1();
        List<Food> expectedFoods = someFoodModelList1();
        foodEntities.forEach(entity -> entity.setFoodImage(new byte[0]));
        expectedFoods.forEach(food -> food.setFoodImage(""));

        //  when
        List<Food> resultFoods = foodEntityMapper.mapFromEntityList(foodEntities);

        //  then
        assertEquals(expectedFoods, resultFoods);
    }

    @Test
    void mapToEntityList_shouldWorkSuccessfully() {
        //  given
        List<FoodEntity> expectedFoodEntities = someFoodEntityList1();
        List<Food> foods = someFoodModelList1();
        expectedFoodEntities.forEach(entity -> entity.setFoodImage(new byte[0]));
        foods.forEach(food -> food.setFoodImage(""));

        //  when
        List<FoodEntity> resultFoodEntities = foodEntityMapper.mapToEntityList(foods);

        //  then
        assertEquals(expectedFoodEntities, resultFoodEntities);
    }
}