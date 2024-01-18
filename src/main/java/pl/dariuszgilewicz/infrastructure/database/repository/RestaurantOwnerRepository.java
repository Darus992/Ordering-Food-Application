package pl.dariuszgilewicz.infrastructure.database.repository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.dariuszgilewicz.infrastructure.database.entity.RestaurantOwnerEntity;
import pl.dariuszgilewicz.infrastructure.database.repository.jpa.RestaurantOwnerJpaRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.mapper.RestaurantOwnerEntityMapper;
import pl.dariuszgilewicz.infrastructure.model.RestaurantOwner;

@Repository
@AllArgsConstructor
public class RestaurantOwnerRepository {

    private RestaurantOwnerJpaRepository restaurantOwnerJpaRepository;
    private RestaurantOwnerEntityMapper restaurantOwnerEntityMapper;

    public void createRestaurantOwner(RestaurantOwner restaurantOwner) {
        RestaurantOwnerEntity toSave = restaurantOwnerEntityMapper.mapToEntity(restaurantOwner);
        restaurantOwnerJpaRepository.save(toSave);
    }

    public RestaurantOwnerEntity findRestaurantOwnerEntityByPesel(String pesel) {
        return restaurantOwnerJpaRepository.findByPesel(pesel)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant Owner with pesel: [%s] not found".formatted(pesel)));
    }
}
