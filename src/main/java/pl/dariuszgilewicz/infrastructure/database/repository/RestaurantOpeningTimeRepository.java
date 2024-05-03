package pl.dariuszgilewicz.infrastructure.database.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.dariuszgilewicz.infrastructure.database.entity.RestaurantOpeningTimeEntity;
import pl.dariuszgilewicz.infrastructure.database.repository.jpa.RestaurantOpeningTimeJpaRepository;

@Repository
@AllArgsConstructor
public class RestaurantOpeningTimeRepository {

    private RestaurantOpeningTimeJpaRepository restaurantOpeningTimeJpaRepository;

    public RestaurantOpeningTimeEntity saveAndReturn(RestaurantOpeningTimeEntity entity){
        restaurantOpeningTimeJpaRepository.save(entity);
        return entity;
    }
}
