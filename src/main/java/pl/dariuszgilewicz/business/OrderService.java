package pl.dariuszgilewicz.business;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dariuszgilewicz.api.dto.FoodDTO;
import pl.dariuszgilewicz.infrastructure.database.entity.CustomerEntity;
import pl.dariuszgilewicz.infrastructure.database.entity.FoodEntity;
import pl.dariuszgilewicz.infrastructure.database.entity.OrdersEntity;
import pl.dariuszgilewicz.infrastructure.database.entity.RestaurantEntity;
import pl.dariuszgilewicz.infrastructure.database.enums.OrderStatus;
import pl.dariuszgilewicz.infrastructure.database.repository.CustomerRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.FoodRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.OrderRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.RestaurantRepository;
import pl.dariuszgilewicz.infrastructure.security.User;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
@AllArgsConstructor
public class OrderService {
    private FoodRepository foodRepository;
    private OrderRepository orderRepository;
    private CustomerRepository customerRepository;
    private RestaurantRepository restaurantRepository;

    @Transactional
    public Integer createOrderAndReturnOrderNumber(
            Integer[] foodsId,
            Integer[] foodsValues,
            BigDecimal totalPrice,
            String orderNotes,
            User user,
            String email
    ) {
        Integer generateRandomNumber = generateRandomNumber();
        OffsetDateTime receivedDateTime = OffsetDateTime.now();
        Map<FoodEntity, Integer> request = new HashMap<>();
        CustomerEntity customerEntity = customerRepository.findCustomerEntityByPhone(user.getCustomer().getPhone());
        RestaurantEntity restaurantEntity = restaurantRepository.findRestaurantEntityByEmail(email);

        for (int i = 0; i < foodsId.length; i++) {
            FoodEntity foodEntity = foodRepository.findFoodEntityById(foodsId[i]);
            request.put(foodEntity, foodsValues[i]);
        }

        OrdersEntity ordersEntity = OrdersEntity.builder()
                .orderNumber(generateRandomNumber)
                .status(OrderStatus.IN_PROGRESS)
                .orderNotes(orderNotes)
                .receivedDateTime(receivedDateTime)
                .orderFoods(request)
                .totalPrice(totalPrice)
                .customer(customerEntity)
                .restaurant(restaurantEntity)
                .build();

        orderRepository.saveOrdersEntity(ordersEntity);
        return generateRandomNumber;
    }

    @Transactional
    public void updateOrderStatus(Integer orderNumber, String status) {
        OrdersEntity ordersEntity = orderRepository.findOrderEntityByOrderNumber(orderNumber);
        OrderStatus orderStatus;

        if (ordersEntity.getStatus().equals(OrderStatus.COMPLETED)) {
            throw new IllegalStateException("Order Entity with number: [%s] is already completed"
                    .formatted(ordersEntity.getOrderNumber()));
        }

        if (status.equals(OrderStatus.ON_THE_WAY.name())) {
            orderStatus = OrderStatus.ON_THE_WAY;
        } else {
            orderStatus = OrderStatus.COMPLETED;
            OffsetDateTime completedDataTime = OffsetDateTime.now();
            ordersEntity.setCompletedDateTime(completedDataTime);
        }
        ordersEntity.setStatus(orderStatus);
        orderRepository.saveOrdersEntity(ordersEntity);
    }

    private Integer generateRandomNumber() {
        Random random = new Random();
        StringBuilder stringNumber = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int nextInt = random.nextInt(0, 9);
            stringNumber.append(nextInt);
        }
        return Integer.parseInt(stringNumber.toString());
    }
}
