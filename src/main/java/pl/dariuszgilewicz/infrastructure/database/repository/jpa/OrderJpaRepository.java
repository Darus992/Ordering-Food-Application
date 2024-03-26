package pl.dariuszgilewicz.infrastructure.database.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.dariuszgilewicz.infrastructure.database.entity.OrdersEntity;

import java.util.Optional;

@Repository
public interface OrderJpaRepository extends JpaRepository<OrdersEntity, Long> {
    Optional<OrdersEntity> findByOrderNumber(Integer orderNumber);
}
