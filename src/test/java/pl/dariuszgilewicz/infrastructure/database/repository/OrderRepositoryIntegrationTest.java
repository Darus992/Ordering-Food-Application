package pl.dariuszgilewicz.infrastructure.database.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.transaction.annotation.Transactional;
import pl.dariuszgilewicz.configuration.AbstractSpringBootIT;
import pl.dariuszgilewicz.infrastructure.database.entity.FoodEntity;
import pl.dariuszgilewicz.infrastructure.database.entity.OrdersEntity;
import pl.dariuszgilewicz.infrastructure.database.repository.jpa.CustomerJpaRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.jpa.FoodJpaRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.jpa.OrderJpaRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.jpa.RestaurantJpaRepository;
import pl.dariuszgilewicz.infrastructure.model.Orders;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static pl.dariuszgilewicz.util.FoodMenuFixtures.someFoodMenuEntity4;
import static pl.dariuszgilewicz.util.OrdersFixtures.someOrdersEntity1;

class OrderRepositoryIntegrationTest extends AbstractSpringBootIT {

    @Autowired
    private RestaurantJpaRepository restaurantJpaRepository;
    @Autowired
    private OrderJpaRepository orderJpaRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CustomerJpaRepository customerJpaRepository;
    @Autowired
    private FoodJpaRepository foodJpaRepository;

    private OrdersEntity ordersEntity;

    @BeforeEach
    void setUp() {
        ordersEntity = prepareOrdersEntity();
        saveDependencies(ordersEntity);
    }

    @AfterEach
    void tearDown() {
        deleteDependencies();
    }

    @Test
    void saveOrdersEntity_shouldWorkSuccessfully() {
        orderRepository.saveOrdersEntity(ordersEntity);

        // then
        List<OrdersEntity> persistedOrders = orderJpaRepository.findAll();

        assertNotNull(persistedOrders.get(0));
        assertEquals(ordersEntity.getOrderNumber(), persistedOrders.get(0).getOrderNumber());
        assertEquals(ordersEntity.getStatus(), persistedOrders.get(0).getStatus());
        assertEquals(1, persistedOrders.size());

    }

    @Test
    void saveOrdersEntity_shouldThrowExceptionWhenForeignKeyIsNullValue() {
        //  given
        ordersEntity.setCustomer(null);

        //  when
        assertThrows(DataIntegrityViolationException.class, () -> orderRepository.saveOrdersEntity(ordersEntity));

        // then
        List<OrdersEntity> all = orderJpaRepository.findAll();
        assertEquals(0, all.size());
    }

    @Test
    void deleteOrdersEntity_shouldWorkSuccessfully() {
        //  given
        orderJpaRepository.save(ordersEntity);

        //  when
        orderRepository.deleteOrdersEntity(ordersEntity);

        //  then
        List<OrdersEntity> all = orderJpaRepository.findAll();
        assertEquals(0, all.size());
    }

    @Test
    void deleteOrdersEntity_shouldThrowExceptionWhenOrderEntityIsNull() {
        //  given
        //  when
        assertThrows(InvalidDataAccessApiUsageException.class, () -> orderRepository.deleteOrdersEntity(null));

        //  then
        List<OrdersEntity> all = orderJpaRepository.findAll();
        assertEquals(0, all.size());
    }

    @Test
    @Transactional
    void findOrderByOrderNumber_shouldReturnOrder_whenOrderExists() {
        // given
        orderJpaRepository.save(ordersEntity);

        Integer orderNumber = ordersEntity.getOrderNumber();

        // when
        Orders resultOrder = orderRepository.findOrderByOrderNumber(orderNumber);

        // then
        List<OrdersEntity> all = orderJpaRepository.findAll();

        assertNotNull(resultOrder);
        assertEquals(ordersEntity.getOrderNumber(), resultOrder.getOrderNumber());
        assertEquals(ordersEntity.getStatus(), resultOrder.getStatus());
        assertEquals(ordersEntity.getOrderFoods().size(), resultOrder.getFoods().size());
        assertEquals(1, all.size());
    }

    @Test
    @Transactional
    void findOrderByOrderNumber_shouldThrowException_whenOrderDoesNotExist() {
        // given
        Integer nonExistentOrderNumber = 99999;

        // when
        // then
        org.assertj.core.api.Assertions.assertThatThrownBy(() -> orderRepository.findOrderByOrderNumber(nonExistentOrderNumber))
                .hasMessageContaining("OrderEntity with orderNumber: [%s] not found".formatted(nonExistentOrderNumber));

        //  verify persistence
        List<OrdersEntity> all = orderJpaRepository.findAll();
        assertEquals(0, all.size());
    }

    @Test
    @Transactional
    void findOrderEntityByOrderNumber_shouldWorkSuccessfully() {
        //  given
        orderJpaRepository.save(ordersEntity);

        Integer orderNumber = ordersEntity.getOrderNumber();

        //  when
        OrdersEntity resultEntity = orderRepository.findOrderEntityByOrderNumber(orderNumber);

        //  then
        List<OrdersEntity> all = orderJpaRepository.findAll();

        assertNotNull(resultEntity);
        assertEquals(ordersEntity.getOrderNumber(), resultEntity.getOrderNumber());
        assertEquals(ordersEntity.getStatus(), resultEntity.getStatus());
        assertEquals(ordersEntity.getOrderFoods().size(), resultEntity.getOrderFoods().size());
        assertEquals(1, all.size());
    }

    @Test
    @Transactional
    void findOrderEntityByOrderNumber_shouldThrowException_whenOrderDoesNotExist() {
        // given
        Integer nonExistentOrderNumber = 99999;

        // when
        // then
        org.assertj.core.api.Assertions.assertThatThrownBy(() -> orderRepository.findOrderEntityByOrderNumber(nonExistentOrderNumber))
                .hasMessageContaining("OrderEntity with orderNumber: [%s] not found".formatted(nonExistentOrderNumber));

        //  verify persistence
        List<OrdersEntity> all = orderJpaRepository.findAll();
        assertEquals(0, all.size());
    }

    private OrdersEntity prepareOrdersEntity() {
        OrdersEntity ordersEntity = someOrdersEntity1();
        ordersEntity.getRestaurant().setFoodMenu(someFoodMenuEntity4());
        return ordersEntity;
    }

    private void saveDependencies(OrdersEntity ordersEntity) {
        customerJpaRepository.save(ordersEntity.getCustomer());
        restaurantJpaRepository.save(ordersEntity.getRestaurant());

        Map<FoodEntity, Integer> orderFoods = ordersEntity.getOrderFoods();
        orderFoods.keySet().forEach(foodEntity -> foodJpaRepository.save(foodEntity));
    }

    private void deleteDependencies() {
        orderJpaRepository.deleteAll();
        customerJpaRepository.deleteAll();
        restaurantJpaRepository.deleteAll();
    }
}