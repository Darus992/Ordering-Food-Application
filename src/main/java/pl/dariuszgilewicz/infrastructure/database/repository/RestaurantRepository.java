package pl.dariuszgilewicz.infrastructure.database.repository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.dariuszgilewicz.infrastructure.database.entity.*;
import pl.dariuszgilewicz.infrastructure.database.repository.jpa.RestaurantJpaRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.mapper.RestaurantEntityMapper;
import pl.dariuszgilewicz.infrastructure.model.Restaurant;
import pl.dariuszgilewicz.infrastructure.request_form.BusinessRequestForm;
import pl.dariuszgilewicz.infrastructure.request_form.RestaurantRequestForm;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class RestaurantRepository {

    private RestaurantEntityMapper restaurantEntityMapper;
    private RestaurantJpaRepository restaurantJpaRepository;
    private RestaurantOwnerRepository restaurantOwnerRepository;

    public RestaurantEntity findRestaurantEntityByEmail(String restaurantEmail){
        return restaurantJpaRepository.findByEmail(restaurantEmail)
                .orElseThrow(() -> new EntityNotFoundException(
                        "RestaurantEntity with restaurantEmail: [%s] not found".formatted(restaurantEmail)
                ));
    }

    public void assignFoodMenuToRestaurant(String restaurantEmail, FoodMenuEntity foodMenuEntity) {
        Optional<RestaurantEntity> optionalRestaurantEntity = restaurantJpaRepository.findByEmail(restaurantEmail);
        if (optionalRestaurantEntity.isEmpty()) {
            throw new EntityNotFoundException("Restaurant with email: [%s] not found".formatted(restaurantEmail));
        }

        RestaurantEntity restaurantEntity = optionalRestaurantEntity.get();
        restaurantEntity.setFoodMenu(foodMenuEntity);
        restaurantJpaRepository.save(restaurantEntity);
    }

    public void assignFoodToFoodMenuInRestaurant(String restaurantEmail, FoodEntity foodEntity) {
        Optional<RestaurantEntity> optionalRestaurantEntity = restaurantJpaRepository.findByEmail(restaurantEmail);
        if (optionalRestaurantEntity.isEmpty()) {
            throw new EntityNotFoundException("Restaurant with email: [%s] not found".formatted(restaurantEmail));
        }

        RestaurantEntity restaurantEntity = optionalRestaurantEntity.get();
        List<FoodEntity> foodEntities = restaurantEntity.getFoodMenu().getFoods();
        foodEntities.add(foodEntity);
        restaurantEntity.getFoodMenu().setFoods(foodEntities);
        restaurantJpaRepository.save(restaurantEntity);
    }

    public List<Restaurant> findAllRestaurantsWithSelectedCategory(List<FoodMenuEntity> foodMenus) {
        return restaurantJpaRepository.findByFoodMenus(foodMenus)
                .map(restaurantEntityMapper::mapAllFromEntity)
                .orElseThrow(() -> new EntityNotFoundException("Not found List of RestaurantEntity by FoodMenuEntity List: [%s]".formatted(foodMenus)));
    }

    public List<Restaurant> findRestaurantsNearYouByAddress(List<AddressEntity> addressEntities) {
        return restaurantJpaRepository.findAllByAddress(addressEntities)
                .map(restaurantEntityMapper::mapAllFromEntity)
                .orElseThrow(() -> new EntityNotFoundException("Not found List of RestaurantEntity by AddressEntity List: [%s]".formatted(addressEntities)));
    }

    public void createRestaurantFromBusinessRequest(BusinessRequestForm businessRequestForm, RestaurantOwnerEntity owner) {
        RestaurantEntity toSave = null;
        try {
            toSave = restaurantEntityMapper.mapFromBusinessRequest(businessRequestForm);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        toSave.setRestaurantOwner(owner);
        restaurantJpaRepository.save(toSave);
    }

    public void createRestaurantFromRestaurantRequest(RestaurantRequestForm restaurantRequestForm, String ownerPesel) {
        RestaurantOwnerEntity ownerEntity = restaurantOwnerRepository.findRestaurantOwnerEntityByPesel(ownerPesel);
        RestaurantEntity toSave = restaurantEntityMapper.mapFromRestaurantRequest(restaurantRequestForm, ownerEntity);
        restaurantJpaRepository.save(toSave);
    }
}
