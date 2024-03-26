package pl.dariuszgilewicz.infrastructure.database.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.dariuszgilewicz.infrastructure.database.entity.CartEntity;

@Repository
public interface CartJpaRepository extends JpaRepository<CartEntity, Integer> {
}
