package pl.dariuszgilewicz.business;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dariuszgilewicz.infrastructure.database.entity.RestaurantEntity;
import pl.dariuszgilewicz.infrastructure.database.repository.RestaurantRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.mapper.RestaurantEntityMapper;
import pl.dariuszgilewicz.infrastructure.model.Restaurant;

import java.util.List;

@Service
@AllArgsConstructor
public class RestaurantService {

    private RestaurantRepository restaurantRepository;
    private RestaurantEntityMapper restaurantEntityMapper;

    @Transactional
    public void createRestaurant(String userName, Restaurant restaurant){
        restaurantRepository.saveAndAssignToUser(userName, restaurant);
    }

    @Transactional
    public List<Restaurant> findRestaurantsByName(String restaurantName) {
        return restaurantRepository.findRestaurantsNyName(restaurantName);
    }

    public Restaurant findRestaurantByEmail(String restaurantEmail) {
        RestaurantEntity entity = restaurantRepository.findRestaurantByEmail(restaurantEmail);
        return restaurantEntityMapper.mapFromEntity(entity);
    }
}
