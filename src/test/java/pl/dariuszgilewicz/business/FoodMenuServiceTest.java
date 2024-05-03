package pl.dariuszgilewicz.business;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.dariuszgilewicz.infrastructure.database.entity.FoodEntity;
import pl.dariuszgilewicz.infrastructure.database.entity.FoodMenuEntity;
import pl.dariuszgilewicz.infrastructure.database.repository.FoodMenuRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.FoodRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.RestaurantRepository;
import pl.dariuszgilewicz.infrastructure.model.Food;
import pl.dariuszgilewicz.infrastructure.model.FoodMenu;

import static org.mockito.Mockito.*;
import static pl.dariuszgilewicz.util.FoodFixtures.someFood1;
import static pl.dariuszgilewicz.util.FoodFixtures.someFoodEntity1;
import static pl.dariuszgilewicz.util.FoodMenuFixtures.someFoodMenu2;
import static pl.dariuszgilewicz.util.FoodMenuFixtures.someFoodMenuEntity2;

@ExtendWith(MockitoExtension.class)
class FoodMenuServiceTest {

    @InjectMocks
    private FoodMenuService foodMenuService;

    @Mock
    private FoodMenuRepository foodMenuRepository;

    @Mock
    private FoodRepository foodRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Test
    void createFoodMenuAndAssignToRestaurant_shouldWorkSuccessfully(){
        //  given
        FoodMenuEntity foodMenuEntity = someFoodMenuEntity2();
        FoodMenu foodMenu = someFoodMenu2();
        String email = "na_wypasie@restaurant.pl";
        when(foodMenuRepository.createAndReturnFoodMenuEntity(foodMenu)).thenReturn(foodMenuEntity);

        //  when
        foodMenuService.createFoodMenuAndAssignToRestaurant(foodMenu, email);

        //  then
        verify(restaurantRepository, times(1)).assignFoodMenuToRestaurant(eq(email), eq(foodMenuEntity));

    }
}