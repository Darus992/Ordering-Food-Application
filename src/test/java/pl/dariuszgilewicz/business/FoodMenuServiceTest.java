package pl.dariuszgilewicz.business;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.dariuszgilewicz.infrastructure.database.entity.FoodEntity;
import pl.dariuszgilewicz.infrastructure.database.entity.FoodMenuEntity;
import pl.dariuszgilewicz.infrastructure.database.entity.RestaurantEntity;
import pl.dariuszgilewicz.infrastructure.database.repository.FoodMenuRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.FoodRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.RestaurantRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.jpa.FoodJpaRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.jpa.FoodMenuJpaRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.jpa.RestaurantJpaRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.mapper.FoodEntityMapper;
import pl.dariuszgilewicz.infrastructure.model.Food;
import pl.dariuszgilewicz.infrastructure.model.FoodMenu;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static pl.dariuszgilewicz.util.FoodFixtures.someFoodEntity1;
import static pl.dariuszgilewicz.util.FoodFixtures.someFoodModel1;
import static pl.dariuszgilewicz.util.FoodMenuFixtures.*;
import static pl.dariuszgilewicz.util.RestaurantFixtures.someRestaurantEntity1;
import static pl.dariuszgilewicz.util.RestaurantFixtures.someRestaurantEntity2;

@ExtendWith(MockitoExtension.class)
class FoodMenuServiceTest {

    @InjectMocks
    private FoodMenuService foodMenuService;
    @Mock
    private RestaurantRepository restaurantRepository;
    @Mock
    private FoodMenuRepository foodMenuRepository;
    @Mock
    private FoodRepository foodRepository;
    @Mock
    private RestaurantJpaRepository restaurantJpaRepository;
    @Mock
    private FoodMenuJpaRepository foodMenuJpaRepository;
    @Mock
    private FoodEntityMapper foodEntityMapper;
    @Mock
    private FoodJpaRepository foodJpaRepository;


    @Test
    void createFoodMenuAndAssignToRestaurant_shouldWorkSuccessfully() {
        //  given
        FoodMenuEntity expectedFoodMenuEntity = someFoodMenuEntity2();
        FoodMenu foodMenu = someFoodMenuModel2();
        String restaurantEmail = "na_wypasie@restaurant.pl";

        when(foodMenuRepository.createAndReturnFoodMenuEntity(foodMenu)).thenReturn(expectedFoodMenuEntity);

        //  when
        foodMenuService.createFoodMenuAndAssignToRestaurant(foodMenu, restaurantEmail);

        //  then
        ArgumentCaptor<FoodMenuEntity> foodMenuEntityArgumentCaptor = ArgumentCaptor.forClass(FoodMenuEntity.class);

        verify(foodMenuRepository, times(1)).createAndReturnFoodMenuEntity(foodMenu);
        verify(restaurantRepository, times(1)).assignFoodMenuToRestaurant(eq(restaurantEmail), foodMenuEntityArgumentCaptor.capture());

        assertEquals(expectedFoodMenuEntity, foodMenuEntityArgumentCaptor.getValue());

    }

    @Test
    void deleteFoodFromMenu_shouldWorkSuccessfully() {
        //  given
        int foodId = 4;
        String restaurantEmail = "na_wypasie@restaurant.pl";
        FoodMenuEntity expectedFoodMenuEntity = someFoodMenuEntity2();
        RestaurantEntity expectedRestaurantEntity = someRestaurantEntity1();

        expectedRestaurantEntity.setFoodMenu(expectedFoodMenuEntity);

        when(restaurantJpaRepository.findByEmail(restaurantEmail)).thenReturn(Optional.of(expectedRestaurantEntity));

        //  when
        foodMenuService.deleteFoodFromMenu(foodId, restaurantEmail);

        //  then
        verify(foodMenuJpaRepository, times(1)).save(expectedFoodMenuEntity);
        assertEquals(expectedRestaurantEntity.getFoodMenu().getFoods().size(), 1);
    }

    @ParameterizedTest
    @MethodSource("provideFoodAndRestaurantData")
    void assignFoodToFoodMenu_shouldWorkSuccessfully(Food food, RestaurantEntity restaurantEntity, boolean foodExists, int expectedFoodCount) {
        //  given
        FoodEntity foodEntity = someFoodEntity1();

        when(foodEntityMapper.mapToEntity(food)).thenReturn(foodEntity);

        if (foodExists) {
            when(foodJpaRepository.findEntityByHisFields(
                    foodEntity.getCategory(),
                    foodEntity.getName(),
                    foodEntity.getDescription(),
                    foodEntity.getPrice(),
                    foodEntity.getFoodImage()
            )).thenReturn(Optional.of(foodEntity));
        } else {
            when(foodJpaRepository.findEntityByHisFields(
                    foodEntity.getCategory(),
                    foodEntity.getName(),
                    foodEntity.getDescription(),
                    foodEntity.getPrice(),
                    foodEntity.getFoodImage()
            )).thenReturn(Optional.empty());
            when(foodRepository.saveAndReturn(foodEntity)).thenReturn(foodEntity);
        }

        //  when
        foodMenuService.assignFoodToFoodMenu(food, restaurantEntity);

        //  then
        ArgumentCaptor<FoodMenuEntity> foodMenuEntityCaptor = ArgumentCaptor.forClass(FoodMenuEntity.class);
        verify(foodMenuJpaRepository, times(1)).save(foodMenuEntityCaptor.capture());

        FoodMenuEntity savedFoodMenuEntity = foodMenuEntityCaptor.getValue();
        assertEquals(expectedFoodCount, savedFoodMenuEntity.getFoods().size());
        assertTrue(savedFoodMenuEntity.getFoods().contains(foodEntity));
    }

    @Test
    void editFoodFromMenu_shouldWorkSuccessfully() {
        //  given
        Food food = someFoodModel1();
        FoodEntity foodEntity = someFoodEntity1();

        when(foodEntityMapper.mapToEntity(food)).thenReturn(foodEntity);
        when(foodJpaRepository.findEntityByHisFields(
                foodEntity.getCategory(),
                foodEntity.getName(),
                foodEntity.getDescription(),
                foodEntity.getPrice(),
                foodEntity.getFoodImage()
        )).thenReturn(Optional.of(foodEntity));

        //  when
        foodMenuService.editFoodFromMenu(food);

        //  then
        verify(foodEntityMapper, times(1)).mapToEntity(food);
        verify(foodJpaRepository, times(1)).findEntityByHisFields(
                foodEntity.getCategory(),
                foodEntity.getName(),
                foodEntity.getDescription(),
                foodEntity.getPrice(),
                foodEntity.getFoodImage()
        );
        verify(foodRepository, never()).saveAndReturn(foodEntity);
    }

    private static Stream<Arguments> provideFoodAndRestaurantData() {
        Food existingFood = someFoodModel1();
        FoodMenuEntity foodMenuWithFood = someFoodMenuEntity2();

        RestaurantEntity restaurantWithFood = someRestaurantEntity1();
        restaurantWithFood.setFoodMenu(foodMenuWithFood);

        RestaurantEntity restaurantWithoutFood = someRestaurantEntity2();
        restaurantWithoutFood.setFoodMenu(someFoodMenuEntity3());

        return Stream.of(
                Arguments.of(existingFood, restaurantWithFood, true, 2),
                Arguments.of(existingFood, restaurantWithoutFood, false, 3)
        );
    }

}