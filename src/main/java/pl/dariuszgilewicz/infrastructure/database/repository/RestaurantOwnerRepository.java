package pl.dariuszgilewicz.infrastructure.database.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.dariuszgilewicz.infrastructure.database.entity.RestaurantOwnerEntity;
import pl.dariuszgilewicz.infrastructure.database.repository.jpa.RestaurantOwnerJpaRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.mapper.RestaurantOwnerEntityMapper;
import pl.dariuszgilewicz.infrastructure.model.RestaurantOwner;
import pl.dariuszgilewicz.infrastructure.security.UserEntity;

@Repository
@AllArgsConstructor
public class RestaurantOwnerRepository {

    private RestaurantOwnerJpaRepository restaurantOwnerJpaRepository;
    private RestaurantOwnerEntityMapper restaurantOwnerEntityMapper;

    public void saveRestaurantOwner(RestaurantOwner restaurantOwner) {
        RestaurantOwnerEntity toSave = restaurantOwnerEntityMapper.mapToEntity(restaurantOwner);
        restaurantOwnerJpaRepository.save(toSave);
    }
}
