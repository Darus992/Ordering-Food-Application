package pl.dariuszgilewicz.infrastructure.database.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.dariuszgilewicz.infrastructure.database.entity.RestaurantEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantJpaRepository extends JpaRepository<RestaurantEntity, Long> {
    Optional<RestaurantEntity> findByEmail(String restaurantEmail);

    Optional<List<RestaurantEntity>> findAllByRestaurantName(String restaurantName);
}
