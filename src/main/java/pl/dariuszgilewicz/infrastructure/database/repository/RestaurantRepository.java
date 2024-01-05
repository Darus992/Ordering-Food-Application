package pl.dariuszgilewicz.infrastructure.database.repository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import pl.dariuszgilewicz.infrastructure.database.entity.RestaurantEntity;
import pl.dariuszgilewicz.infrastructure.database.repository.jpa.RestaurantJpaRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.mapper.RestaurantEntityMapper;
import pl.dariuszgilewicz.infrastructure.model.Restaurant;
import pl.dariuszgilewicz.infrastructure.security.UserEntity;
import pl.dariuszgilewicz.infrastructure.security.UserJpaRepository;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class RestaurantRepository {

    private RestaurantEntityMapper restaurantEntityMapper;
    private RestaurantJpaRepository restaurantJpaRepository;
    private UserJpaRepository userJpaRepository;


    public List<Restaurant> findRestaurantsNyName(String restaurantName) {
        Optional<List<RestaurantEntity>> allByName = restaurantJpaRepository.findAllByRestaurantName(restaurantName);
        if(allByName.isEmpty()){

            //DODAĆ OBSŁUGĘ BŁĘDU W SYTUACJI KIEDY NIE ZNALEŹLIŚMY RESTAURACJI!
            return null;
        }else {
            return restaurantEntityMapper.mapAllFromEntity(allByName.get());
        }
    }
    
    public void saveAndAssignToUser(String userName, Restaurant restaurant) {
        RestaurantEntity toSave = restaurantEntityMapper.mapToEntity(restaurant);
        UserEntity userEntity = userJpaRepository.findByUserName(userName)
                .orElseThrow(() -> new UsernameNotFoundException("USER NOT FOUND"));
        toSave.setUser(userEntity);
        restaurantJpaRepository.save(toSave);
    }

    public RestaurantEntity findRestaurantByEmail(String email) {
        return restaurantJpaRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant with email: [%s] not found".formatted(email)));
    }

    public void save(RestaurantEntity restaurantEntity) {
        restaurantJpaRepository.save(restaurantEntity);
    }
}
