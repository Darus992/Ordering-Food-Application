package pl.dariuszgilewicz.infrastructure.database.repository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.dariuszgilewicz.infrastructure.database.entity.*;
import pl.dariuszgilewicz.infrastructure.database.repository.jpa.AddressJpaRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.jpa.FoodMenuJpaRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.jpa.RestaurantJpaRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.mapper.RestaurantEntityMapper;
import pl.dariuszgilewicz.infrastructure.model.Restaurant;
import pl.dariuszgilewicz.infrastructure.request_form.BusinessRequestForm;
import pl.dariuszgilewicz.infrastructure.request_form.RestaurantRequestForm;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class RestaurantRepository {

    private RestaurantEntityMapper restaurantEntityMapper;
    private RestaurantJpaRepository restaurantJpaRepository;
    private FoodMenuJpaRepository foodMenuJpaRepository;
    private AddressJpaRepository addressJpaRepository;
    private RestaurantOwnerRepository restaurantOwnerRepository;


    public List<Restaurant> findRestaurantsByName(String restaurantName) {
        Optional<List<RestaurantEntity>> allByName = restaurantJpaRepository.findAllByRestaurantName(restaurantName);
        if (allByName.isEmpty()) {
            throw new EntityNotFoundException("Not found restaurants list with restaurant name: [%s]".formatted(restaurantName));
        } else {
            return restaurantEntityMapper.mapAllFromEntity(allByName.get());
        }
    }

    public RestaurantEntity findRestaurantByEmail(String email) {
        return restaurantJpaRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant with email: [%s] not found".formatted(email)));
    }

    public void assignFoodMenuToRestaurant(String restaurantEmail, FoodMenuEntity foodMenuEntity) {
        RestaurantEntity restaurantEntity = findRestaurantByEmail(restaurantEmail);
        restaurantEntity.setFoodMenu(foodMenuEntity);
        restaurantJpaRepository.save(restaurantEntity);
    }

    public void assignFoodToFoodMenuInRestaurant(String restaurantEmail, FoodEntity foodEntity) {
        RestaurantEntity restaurantEntity = findRestaurantByEmail(restaurantEmail);
        List<FoodEntity> foodEntities = restaurantEntity.getFoodMenu().getFoods();
        foodEntities.add(foodEntity);
        restaurantEntity.getFoodMenu().setFoods(foodEntities);
        restaurantJpaRepository.save(restaurantEntity);
    }

    public List<Restaurant> findAllRestaurantsWithPickedCategory(String categoryName) {
        List<FoodMenuEntity> foodMenus = foodMenuJpaRepository.findAllByCategory(categoryName)
                .orElseThrow(() -> new EntityNotFoundException("Not found List of FoodMenuEntity by category name: [%s]".formatted(categoryName)));

        List<RestaurantEntity> restaurantEntities = restaurantJpaRepository.findByFoodMenus(foodMenus)
                .orElseThrow(() -> new EntityNotFoundException("Not found List of RestaurantEntity by FoodMenuEntity List: [%s]".formatted(foodMenus)));

        return restaurantEntityMapper.mapAllFromEntity(restaurantEntities);
    }

    public List<Restaurant> findAllRestaurants() {
        List<RestaurantEntity> restaurantEntities = restaurantJpaRepository.findAll();
        return restaurantEntityMapper.mapAllFromEntity(restaurantEntities);
    }

    public List<Restaurant> findRestaurantsNearYouByAddress(String searchTerm) {
        List<AddressEntity> addressEntities = addressJpaRepository.findBySearchTerm(searchTerm)
                .orElseThrow(() -> new EntityNotFoundException("Not found List of AddressEntity by searchTerm: [%s]".formatted(searchTerm)));

        List<RestaurantEntity> restaurantEntities = restaurantJpaRepository.findAllByAddress(addressEntities)
                .orElseThrow(() -> new EntityNotFoundException("Not found List of RestaurantEntity by AddressEntity List: [%s]".formatted(addressEntities)));

        return restaurantEntityMapper.mapAllFromEntity(restaurantEntities);
    }

    public void createRestaurantFromBusinessRequest(BusinessRequestForm businessRequestForm, RestaurantOwnerEntity owner) {
        RestaurantEntity toSave = restaurantEntityMapper.mapFromBusinessRequest(businessRequestForm, owner);
        restaurantJpaRepository.save(toSave);
    }

    public void createRestaurantFromRestaurantRequest(RestaurantRequestForm restaurantRequestForm, String ownerPesel) {
        RestaurantOwnerEntity ownerEntity = restaurantOwnerRepository.findRestaurantOwnerEntityByPesel(ownerPesel);
        RestaurantEntity toSave = restaurantEntityMapper.mapFromRestaurantRequest(restaurantRequestForm, ownerEntity);
        restaurantJpaRepository.save(toSave);
    }
}
