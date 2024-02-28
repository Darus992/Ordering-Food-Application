package pl.dariuszgilewicz.infrastructure.database.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.dariuszgilewicz.infrastructure.database.entity.RestaurantOwnerEntity;

import java.util.Optional;

@Repository
public interface RestaurantOwnerJpaRepository extends JpaRepository<RestaurantOwnerEntity, Long> {

    Optional<RestaurantOwnerEntity> findByPesel(String pesel);

    boolean existsByPesel(String ownerPesel);
}
