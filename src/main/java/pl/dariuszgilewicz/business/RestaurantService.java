package pl.dariuszgilewicz.business;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dariuszgilewicz.infrastructure.database.entity.RestaurantEntity;
import pl.dariuszgilewicz.infrastructure.database.repository.RestaurantRepository;
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
    private RestaurantEntityMapper restaurantEntityMapper;
    private UserService userService;

    @Transactional
    public List<Restaurant> findRestaurantsByName(String restaurantName) {
        return restaurantRepository.findRestaurantsByName(restaurantName);
    }

    @Transactional
    public Restaurant findRestaurantByEmail(String restaurantEmail) {
        RestaurantEntity entity = restaurantRepository.findRestaurantByEmail(restaurantEmail);
        return restaurantEntityMapper.mapFromEntity(entity);
    }

    @Transactional
    public List<Restaurant> findAllRestaurantsWithPickedCategory(String foodCategory) {
        return restaurantRepository.findAllRestaurantsWithPickedCategory(foodCategory);
    }

    @Transactional
    public List<Restaurant> findAllRestaurants() {
        return restaurantRepository.findAllRestaurants();
    }

    @Transactional
    public List<Restaurant> findRestaurantsNearYouByAddress(String searchTerm) {
        return restaurantRepository.findRestaurantsNearYouByAddress(searchTerm);
    }

    @Transactional
    public void createRestaurantAndAssignToOwner(RestaurantRequestForm restaurantRequestForm, String userName) {
        User user = userService.findUserByUserName(userName);
        String ownerPesel = user.getRestaurantOwner().getPesel();
        restaurantRepository.createRestaurantFromRestaurantRequest(restaurantRequestForm, ownerPesel);
    }
}
