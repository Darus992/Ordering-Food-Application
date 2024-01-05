package pl.dariuszgilewicz.infrastructure.database.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.dariuszgilewicz.infrastructure.database.entity.FoodMenuEntity;

import java.util.Optional;

@Repository
public interface FoodMenuJpaRepository extends JpaRepository<FoodMenuEntity, Long> {

    Optional<FoodMenuEntity> findByMenuName(String menuName);
}
