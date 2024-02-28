package pl.dariuszgilewicz.business;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dariuszgilewicz.infrastructure.database.repository.RestaurantRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.jpa.AddressJpaRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.jpa.FoodMenuJpaRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.jpa.RestaurantJpaRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.mapper.RestaurantEntityMapper;
import pl.dariuszgilewicz.infrastructure.model.Restaurant;
import pl.dariuszgilewicz.infrastructure.request_form.RestaurantRequestForm;
import pl.dariuszgilewicz.infrastructure.security.User;
import pl.dariuszgilewicz.infrastructure.security.UserService;

import java.util.List;

@Service
@AllArgsConstructor
public class RestaurantService {

    private RestaurantRepository restaurantRepository;
    private RestaurantJpaRepository restaurantJpaRepository;
    private FoodMenuJpaRepository foodMenuJpaRepository;
    private AddressJpaRepository addressJpaRepository;
    private RestaurantEntityMapper restaurantEntityMapper;
    private UserService userService;


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
    public List<Restaurant> findRestaurantsNearYouByAddress(String searchTerm) {
        return addressJpaRepository.findBySearchTerm(searchTerm)
                .map(restaurantRepository::findRestaurantsNearYouByAddress)
                .orElseThrow(() -> new EntityNotFoundException("Not found List of AddressEntity by searchTerm: [%s]".formatted(searchTerm)));
    }

    @Transactional
    public void createRestaurantAndAssignToOwner(RestaurantRequestForm restaurantRequestForm, String userName) {
        User user = userService.findUserByUserName(userName);
        String ownerPesel = user.getRestaurantOwner().getPesel();
        restaurantRepository.createRestaurantFromRestaurantRequest(restaurantRequestForm, ownerPesel);
    }
}
