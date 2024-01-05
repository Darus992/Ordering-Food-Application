package pl.dariuszgilewicz.business;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dariuszgilewicz.infrastructure.database.entity.FoodEntity;
import pl.dariuszgilewicz.infrastructure.database.entity.FoodMenuEntity;
import pl.dariuszgilewicz.infrastructure.database.entity.RestaurantEntity;
import pl.dariuszgilewicz.infrastructure.database.repository.FoodMenuRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.FoodRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.RestaurantRepository;
import pl.dariuszgilewicz.infrastructure.model.Food;
import pl.dariuszgilewicz.infrastructure.model.FoodMenu;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class FoodMenuService {

    private RestaurantRepository restaurantRepository;
    private FoodMenuRepository foodMenuRepository;
    private FoodRepository foodRepository;


    @Transactional
    public void createAndAddFoodMenu(FoodMenu foodMenu, String restaurantEmail) {
        FoodMenuEntity menuEntity = foodMenuRepository.createFoodMenu(foodMenu);
        RestaurantEntity restaurantEntity = findRestaurantByEmail(restaurantEmail);
        restaurantEntity.setFoodMenu(menuEntity);
        restaurantRepository.save(restaurantEntity);
    }

    @Transactional
    public void createFoodAndAddToMenu(Food food, String restaurantEmail) {
        FoodEntity entity = foodRepository.createFood(food);
        RestaurantEntity restaurantEntity = restaurantRepository.findRestaurantByEmail(restaurantEmail);

        //  TODO: MOŻE TO JAKOŚ UPROŚCIĆ...?
        List<FoodEntity> foods = restaurantEntity.getFoodMenu().getFoods();
        foods.add(entity);
        restaurantEntity.getFoodMenu().setFoods(foods);
        restaurantRepository.save(restaurantEntity);
    }

    private RestaurantEntity findRestaurantByEmail(String email) {
        return restaurantRepository.findRestaurantByEmail(email);
    }
}
