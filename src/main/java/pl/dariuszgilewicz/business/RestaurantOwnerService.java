package pl.dariuszgilewicz.business;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dariuszgilewicz.infrastructure.database.repository.RestaurantOwnerRepository;
import pl.dariuszgilewicz.infrastructure.model.RestaurantOwner;
import pl.dariuszgilewicz.infrastructure.security.UserRepository;

@Service
@AllArgsConstructor
public class RestaurantOwnerService {

    private RestaurantOwnerRepository restaurantOwnerRepository;
    private UserRepository userRepository;

    @Transactional
    public void createRestaurantOwner(String userName, RestaurantOwner restaurantOwner) {
        restaurantOwnerRepository.saveRestaurantOwner(restaurantOwner);
        assignRestaurantOwner(userName, restaurantOwner.getPesel());
    }

    private void assignRestaurantOwner(String userName, String pesel) {
        userRepository.assignRestaurantOwner(userName, pesel);
    }
}
