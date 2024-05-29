package pl.dariuszgilewicz.business;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FoodMenuService {

    private RestaurantRepository restaurantRepository;
    private FoodMenuRepository foodMenuRepository;
    private FoodRepository foodRepository;
    private RestaurantJpaRepository restaurantJpaRepository;
    private FoodMenuJpaRepository foodMenuJpaRepository;
    private FoodEntityMapper foodEntityMapper;
    private FoodJpaRepository foodJpaRepository;


    @Transactional
    public void createFoodMenuAndAssignToRestaurant(FoodMenu foodMenu, String restaurantEmail) {
        FoodMenuEntity foodMenuEntity = foodMenuRepository.createAndReturnFoodMenuEntity(foodMenu);
        assignFoodMenuToRestaurant(restaurantEmail, foodMenuEntity);
    }

    @Transactional
    public void deleteFoodFromMenu(Integer foodId, String restaurantEmail) {
        Optional<RestaurantEntity> optionalRestaurant = restaurantJpaRepository.findByEmail(restaurantEmail);

        RestaurantEntity restaurantEntity = optionalRestaurant.orElseThrow(() ->
                new EntityNotFoundException("RestaurantEntity with email: [%s] not found.".formatted(restaurantEmail)));

        FoodMenuEntity foodMenuEntity = restaurantEntity.getFoodMenu();
        List<FoodEntity> foods = foodMenuEntity.getFoods();

        Iterator<FoodEntity> iterator = foods.iterator();
        while (iterator.hasNext()) {
            FoodEntity food = iterator.next();
            if (food.getFoodId().equals(foodId)) {
                iterator.remove();
                break;
            }
        }

        foodMenuEntity.setFoods(foods);
        foodMenuJpaRepository.save(foodMenuEntity);
    }

    @Transactional
    public void assignFoodToFoodMenu(Food food, RestaurantEntity restaurantEntity) {
        FoodEntity foodEntity = checkIfFoodExistOrCreateNewAndReturn(food);
        FoodMenuEntity foodMenuEntity = restaurantEntity.getFoodMenu();
        List<FoodEntity> foods = foodMenuEntity.getFoods();

        if (foods.stream().noneMatch(entity -> entity.getFoodId().equals(foodEntity.getFoodId()))) {
            foods.add(foodEntity);
        }

        foodMenuJpaRepository.save(foodMenuEntity);
    }

    @Transactional
    public void editFoodFromMenu(Food food) {
        checkIfFoodExistOrCreateNewAndReturn(food);
    }

    private FoodEntity checkIfFoodExistOrCreateNewAndReturn(Food food) {
        FoodEntity foodEntity = foodEntityMapper.mapToEntity(food);

        Optional<FoodEntity> optionalFoodEntity = foodJpaRepository.findEntityByHisFields(
                foodEntity.getCategory(),
                foodEntity.getName(),
                foodEntity.getDescription(),
                foodEntity.getPrice(),
                foodEntity.getFoodImage()
        );

        return optionalFoodEntity.orElseGet(() -> foodRepository.saveAndReturn(foodEntity));
    }

    private void assignFoodMenuToRestaurant(String restaurantEmail, FoodMenuEntity foodMenuEntity) {
        restaurantRepository.assignFoodMenuToRestaurant(restaurantEmail, foodMenuEntity);
    }
}
