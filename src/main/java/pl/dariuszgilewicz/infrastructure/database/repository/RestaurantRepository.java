package pl.dariuszgilewicz.infrastructure.database.repository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import pl.dariuszgilewicz.infrastructure.database.entity.*;
import pl.dariuszgilewicz.infrastructure.database.repository.jpa.RestaurantJpaRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.jpa.RestaurantOwnerJpaRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.mapper.RestaurantEntityMapper;
import pl.dariuszgilewicz.infrastructure.database.repository.mapper.RestaurantOwnerEntityMapper;
import pl.dariuszgilewicz.infrastructure.model.Restaurant;
import pl.dariuszgilewicz.infrastructure.model.RestaurantOwner;
import pl.dariuszgilewicz.infrastructure.request_form.BusinessRequestForm;
import pl.dariuszgilewicz.infrastructure.request_form.RestaurantRequestForm;
import pl.dariuszgilewicz.infrastructure.util.ImageConverter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class RestaurantRepository {

    private RestaurantEntityMapper restaurantEntityMapper;
    private RestaurantJpaRepository restaurantJpaRepository;
    private RestaurantOwnerJpaRepository restaurantOwnerJpaRepository;
    private RestaurantOwnerEntityMapper restaurantOwnerEntityMapper;

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

//    public List<Restaurant> findAllRestaurantsWithSelectedCategory(List<FoodMenuEntity> foodMenus) {
//        return restaurantJpaRepository.findByFoodMenus(foodMenus)
//                .map(restaurantEntityMapper::mapFromEntityList)
//                .orElseThrow(() -> new EntityNotFoundException("Not found List of RestaurantEntity by FoodMenuEntity List: [%s]".formatted(foodMenus)));
//    }

    public List<Restaurant> findRestaurantsByAddress(List<AddressEntity> addressEntities) {
        return restaurantJpaRepository.findAllByAddress(addressEntities)
                .map(restaurantEntityMapper::mapFromEntityList)
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

    public void createRestaurantFromRestaurantRequest(RestaurantRequestForm restaurantRequestForm, RestaurantOwner owner) {
        Optional<RestaurantOwnerEntity> result = restaurantOwnerJpaRepository.findByPesel(owner.getPesel());
        RestaurantOwnerEntity ownerEntity;
        RestaurantEntity toSave;

        ownerEntity = result.orElseGet(() -> restaurantOwnerEntityMapper.mapToEntity(owner));
        toSave = restaurantEntityMapper.mapFromRestaurantRequest(restaurantRequestForm, ownerEntity);
        restaurantJpaRepository.save(toSave);
    }

    public void updateRestaurantDetails(RestaurantEntity entity, Restaurant restaurant, AddressEntity updateRestaurantAddressDetails) {
        entity.setRestaurantName(restaurant.getRestaurantName());
        entity.setPhone(restaurant.getRestaurantPhone());
        entity.setEmail(restaurant.getRestaurantEmail());
        entity.setRestaurantAddress(updateRestaurantAddressDetails);
        restaurantJpaRepository.save(entity);
    }

    public void updateRestaurantImage(MultipartFile restaurantImage, RestaurantEntity entity, String param) {
        byte[] convertFileToBytes = ImageConverter.convertFileToBytes(restaurantImage);

        if(param.equals("CARD")){
            entity.setRestaurantImageCard(convertFileToBytes);
        }else {
            entity.setRestaurantImageHeader(convertFileToBytes);
        }
        restaurantJpaRepository.save(entity);
    }

    public void updateRestaurantSchedule(RestaurantEntity restaurantEntity, RestaurantOpeningTimeEntity openingTimeEntity) {
        restaurantEntity.setRestaurantOpeningTime(openingTimeEntity);
        restaurantJpaRepository.save(restaurantEntity);
    }
}