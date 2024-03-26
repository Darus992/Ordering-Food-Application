package pl.dariuszgilewicz.infrastructure.database.repository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.dariuszgilewicz.infrastructure.database.entity.OrdersEntity;
import pl.dariuszgilewicz.infrastructure.database.repository.jpa.OrderJpaRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.mapper.OrderEntityMapper;
import pl.dariuszgilewicz.infrastructure.model.Orders;

@Repository
@AllArgsConstructor
public class OrderRepository {

    private OrderJpaRepository orderJpaRepository;
    private OrderEntityMapper orderEntityMapper;

    public void saveOrdersEntity(OrdersEntity orders) {
        orderJpaRepository.save(orders);
    }

    public void deleteOrdersEntity(OrdersEntity orders) {
        orderJpaRepository.delete(orders);
    }

    public Orders findOrderByOrderNumber(Integer orderNumber){
        return orderJpaRepository.findByOrderNumber(orderNumber)
                .map(orderEntityMapper::mapFromEntity)
                .orElseThrow(() -> new EntityNotFoundException(
                        "OrderEntity with orderNumber: [%s] not found".formatted(orderNumber)
                ));
    }

    public OrdersEntity findOrderEntityByOrderNumber(Integer orderNumber) {
        return orderJpaRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new EntityNotFoundException(
                        "OrderEntity with orderNumber: [%s] not found".formatted(orderNumber)
                ));
    }
}
