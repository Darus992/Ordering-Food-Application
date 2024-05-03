package pl.dariuszgilewicz.business;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pl.dariuszgilewicz.infrastructure.database.entity.AddressEntity;
import pl.dariuszgilewicz.infrastructure.database.entity.RestaurantEntity;
import pl.dariuszgilewicz.infrastructure.database.entity.RestaurantOpeningTimeEntity;
import pl.dariuszgilewicz.infrastructure.database.repository.AddressRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.OrderRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.RestaurantOpeningTimeRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.RestaurantRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.jpa.AddressJpaRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.jpa.FoodMenuJpaRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.jpa.RestaurantJpaRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.jpa.RestaurantOpeningTimeJpaRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.mapper.RestaurantEntityMapper;
import pl.dariuszgilewicz.infrastructure.database.repository.mapper.RestaurantOpeningTimeEntityMapper;
import pl.dariuszgilewicz.infrastructure.model.Orders;
import pl.dariuszgilewicz.infrastructure.model.Restaurant;
import pl.dariuszgilewicz.infrastructure.model.RestaurantOwner;
import pl.dariuszgilewicz.infrastructure.request_form.RestaurantRequestForm;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RestaurantService {

    private RestaurantRepository restaurantRepository;
    private AddressRepository addressRepository;
    private RestaurantJpaRepository restaurantJpaRepository;
    private FoodMenuJpaRepository foodMenuJpaRepository;
    private AddressJpaRepository addressJpaRepository;
    private RestaurantEntityMapper restaurantEntityMapper;
    private RestaurantOpeningTimeEntityMapper restaurantOpeningTimeEntityMapper;
    private RestaurantOpeningTimeJpaRepository restaurantOpeningTimeJpaRepository;
    private RestaurantOpeningTimeRepository restaurantOpeningTimeRepository;
    private OrderRepository orderRepository;


    @Transactional
    public Restaurant findRestaurantByEmail(String restaurantEmail) {
        return restaurantJpaRepository.findByEmail(restaurantEmail)
                .map(restaurantEntityMapper::mapFromEntity)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Restaurant with email: [%s] not found".formatted(restaurantEmail)
                ));
    }

    @Transactional
    public List<Restaurant> findAllRestaurantsWithSelectedCategory(String foodCategory) {
        return foodMenuJpaRepository.findAllByCategory(foodCategory)
                .map(restaurantRepository::findAllRestaurantsWithSelectedCategory)
                .orElseThrow(() -> new EntityNotFoundException("Not found List of FoodMenuEntity by category name: [%s]".formatted(foodCategory)));
    }

    @Transactional
    public List<Restaurant> findRestaurantsBySearchTerm(String searchTerm) {
        return addressJpaRepository.findBySearchTerm(searchTerm)
                .map(restaurantRepository::findRestaurantsByAddress)
                .orElseThrow(() -> new EntityNotFoundException("Not found List of AddressEntity by searchTerm: [%s]".formatted(searchTerm)));
    }

    @Transactional
    public void createRestaurantAndAssignToOwner(RestaurantRequestForm restaurantRequestForm, RestaurantOwner owner) {
        restaurantRepository.createRestaurantFromRestaurantRequest(restaurantRequestForm, owner);
    }

    @Transactional
    public void updateRestaurantDetails(RestaurantEntity entity, Restaurant restaurant) {
        AddressEntity updateRestaurantAddressDetails = updateRestaurantAddressDetails(entity, restaurant);
        restaurantRepository.updateRestaurantDetails(entity, restaurant, updateRestaurantAddressDetails);
    }

    @Transactional
    public void updateRestaurantImage(MultipartFile restaurantImage, RestaurantEntity entity, String param) {
        restaurantRepository.updateRestaurantImage(restaurantImage, entity, param);
    }

    @Transactional
    public void updateRestaurantSchedule(RestaurantEntity entity, Restaurant restaurant) {
        RestaurantOpeningTimeEntity result = updateRestaurantOpeningTime(restaurant);
        restaurantRepository.updateRestaurantSchedule(entity, result);
    }

    private AddressEntity updateRestaurantAddressDetails(RestaurantEntity entity, Restaurant restaurant) {
        return addressRepository.updateRestaurantAddressDetails(entity.getRestaurantAddress(), restaurant);
    }

    private RestaurantOpeningTimeEntity updateRestaurantOpeningTime(Restaurant restaurant) {
        RestaurantOpeningTimeEntity requestOpeningTimeEntity = restaurantOpeningTimeEntityMapper.mapToEntity(restaurant.getRestaurantOpeningTime());

        Optional<RestaurantOpeningTimeEntity> entityByTimeAndDay = restaurantOpeningTimeJpaRepository.findEntityByTimeAndDay(
                requestOpeningTimeEntity.getOpeningHour(),
                requestOpeningTimeEntity.getCloseHour(),
                requestOpeningTimeEntity.getDayOfWeekFrom(),
                requestOpeningTimeEntity.getDayOfWeekTill()
        );

        return entityByTimeAndDay.orElseGet(() -> restaurantOpeningTimeRepository.saveAndReturn(requestOpeningTimeEntity));
    }

    @Transactional
    public List<Orders> createOrdersListByOrderNumber(List<Integer> orderNumbers) {
        List<Orders> result = new ArrayList<>();

        for (Integer number : orderNumbers){
            Orders order = orderRepository.findOrderByOrderNumber(number);
            result.add(order);
        }
        return result;
    }
}
