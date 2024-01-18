package pl.dariuszgilewicz.business;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dariuszgilewicz.infrastructure.database.entity.FoodEntity;
import pl.dariuszgilewicz.infrastructure.database.entity.FoodMenuEntity;
import pl.dariuszgilewicz.infrastructure.database.repository.FoodMenuRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.FoodRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.RestaurantRepository;
import pl.dariuszgilewicz.infrastructure.model.Food;
import pl.dariuszgilewicz.infrastructure.model.FoodMenu;

@Service
@AllArgsConstructor
public class FoodMenuService {

    private RestaurantRepository restaurantRepository;
    private FoodMenuRepository foodMenuRepository;
    private FoodRepository foodRepository;


    @Transactional
    public void createFoodMenuAndAssignToRestaurant(FoodMenu foodMenu, String restaurantEmail) {
        FoodMenuEntity foodMenuEntity = foodMenuRepository.createAndReturnFoodMenuEntity(foodMenu);
        assignFoodMenuToRestaurant(restaurantEmail, foodMenuEntity);
    }

    @Transactional
    public void createFoodAndAssignToFoodMenu(Food food, String restaurantEmail) {
        FoodEntity foodEntity = foodRepository.createAndReturnFoodEntity(food);
        assignFoodToFoodMenuInRestaurant(restaurantEmail, foodEntity);
    }

    private void assignFoodMenuToRestaurant(String restaurantEmail, FoodMenuEntity foodMenuEntity) {
        restaurantRepository.assignFoodMenuToRestaurant(restaurantEmail, foodMenuEntity);
    }

    private void assignFoodToFoodMenuInRestaurant(String restaurantEmail, FoodEntity foodEntity) {
        restaurantRepository.assignFoodToFoodMenuInRestaurant(restaurantEmail, foodEntity);
    }
}
