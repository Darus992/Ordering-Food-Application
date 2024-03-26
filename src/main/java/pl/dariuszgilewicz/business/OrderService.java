package pl.dariuszgilewicz.business;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dariuszgilewicz.infrastructure.database.entity.CustomerEntity;
import pl.dariuszgilewicz.infrastructure.database.entity.FoodEntity;
import pl.dariuszgilewicz.infrastructure.database.entity.OrdersEntity;
import pl.dariuszgilewicz.infrastructure.database.entity.RestaurantEntity;
import pl.dariuszgilewicz.infrastructure.database.enums.OrderStatus;
import pl.dariuszgilewicz.infrastructure.database.repository.FoodRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.OrderRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.RestaurantRepository;
import pl.dariuszgilewicz.infrastructure.model.Orders;
import pl.dariuszgilewicz.infrastructure.security.UserRepository;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class OrderService {
    private FoodRepository foodRepository;
    private UserRepository userRepository;
    private RestaurantRepository restaurantRepository;
    private OrderRepository orderRepository;

    @Transactional
    public void createCart(List<Integer> foodIds, List<Integer> quantity, String username, String restaurantEmail) {
        CustomerEntity customer = userRepository.findUserEntityByUsername(username).getCustomer();
        RestaurantEntity restaurant = restaurantRepository.findRestaurantEntityByEmail(restaurantEmail);

        Integer generatedRandomOrderNumber = generateRandomNumbers();
        BigDecimal totalPrice = BigDecimal.ZERO;

        OrdersEntity orders = new OrdersEntity();
        Map<FoodEntity, Integer> orderRequest = new HashMap<>();
        List<Integer> notNullQuantity = getNotNullQuantity(quantity);

        int index = 0;
        totalPrice = addFoodToOrderRequestAndCalculateTotalPrice(foodIds, totalPrice, orderRequest, notNullQuantity, index);

        orders.setOrderNumber(generatedRandomOrderNumber);
        orders.setOrderRequest(orderRequest);
        orders.setTotalPrice(totalPrice);
        orders.setCustomer(customer);
        orders.setRestaurant(restaurant);

        orderRepository.saveOrdersEntity(orders);
    }

    @Transactional
    public void updateCart(Integer orderNumber, List<Integer> foodIds, List<Integer> quantity) {
        OrdersEntity orders = orderRepository.findOrderEntityByOrderNumber(orderNumber);
        BigDecimal totalPrice = orders.getTotalPrice();
        Map<FoodEntity, Integer> updatedOrderRequest = new HashMap<>();
        Map<FoodEntity, Integer> orderRequest = orders.getOrderRequest();
        List<Integer> notNullQuantity = getNotNullQuantity(quantity);

        for (int i = 0; i < foodIds.size(); i++) {
            FoodEntity food = foodRepository.findFoodEntityById(foodIds.get(i));
            Integer quantityValue = notNullQuantity.get(i);

            if (orderRequest.containsKey(food)) {
                Integer currentValue = orderRequest.get(food);
                orderRequest.replace(food, currentValue + quantityValue);
            } else {
                updatedOrderRequest.put(food, quantityValue);
            }

            totalPrice = calculateTotalPrice(totalPrice, food, quantityValue);
        }

        orderRequest.putAll(updatedOrderRequest);
        orders.setOrderRequest(orderRequest);
        orders.setTotalPrice(totalPrice);

        orderRepository.saveOrdersEntity(orders);
    }


    @Transactional
    public boolean deleteFoodFromCart(Integer orderNumber, Integer foodId, Integer quantity) {
        OrdersEntity orders = orderRepository.findOrderEntityByOrderNumber(orderNumber);
        Map<FoodEntity, Integer> orderRequest = orders.getOrderRequest();
        Iterator<Map.Entry<FoodEntity, Integer>> iterator = orderRequest.entrySet().iterator();
        BigDecimal totalPrice = BigDecimal.ZERO;

        while (iterator.hasNext()) {
            Map.Entry<FoodEntity, Integer> entry = iterator.next();
            FoodEntity food = entry.getKey();
            Integer value = entry.getValue();

            if (food.getFoodId().equals(foodId)) {
                totalPrice = removeOrAdjustOrderRequest(quantity, orderRequest, totalPrice, iterator, value, food);
            } else {
                totalPrice = calculateTotalPrice(totalPrice, food, value);
            }
        }

        orders.setTotalPrice(totalPrice);

        if (orders.getOrderRequest().size() == 0) {
            orderRepository.deleteOrdersEntity(orders);
            return true;
        } else {
            orderRepository.saveOrdersEntity(orders);
            return false;
        }
    }

    @Transactional
    public void deleteCart(Integer orderNumber) {
        OrdersEntity order = orderRepository.findOrderEntityByOrderNumber(orderNumber);
        orderRepository.deleteOrdersEntity(order);
    }

    @Transactional
    public void createOrder(Integer orderNumber, Orders tempOrder) {
        OrdersEntity order = orderRepository.findOrderEntityByOrderNumber(orderNumber);
        OffsetDateTime receivedDateTime = OffsetDateTime.now();
        OffsetDateTime expectedDeliveryDateTime = receivedDateTime.plusMinutes(2);


        order.setReceivedDateTime(receivedDateTime);
        order.setExpectedDeliveryDateTime(expectedDeliveryDateTime);
        order.setStatus(OrderStatus.IN_PROGRESS);
        order.setOrderNotes(tempOrder.getOrderNotes());

        orderRepository.saveOrdersEntity(order);
    }

    public void checkForUpdateStatus(Orders order) {
        OrdersEntity orderEntity = orderRepository.findOrderEntityByOrderNumber(order.getOrderNumber());
        OffsetDateTime currentTime = OffsetDateTime.now();

        OffsetDateTime expectedDeliveryDateTime = orderEntity.getExpectedDeliveryDateTime();

        if (currentTime.compareTo(expectedDeliveryDateTime) >= 0) {
            if (orderEntity.getStatus() == OrderStatus.IN_PROGRESS) {
                orderEntity.setStatus(OrderStatus.ON_THE_WAY);
                orderEntity.setExpectedDeliveryDateTime(currentTime.plusMinutes(1));
            } else if (orderEntity.getStatus() == OrderStatus.ON_THE_WAY) {
                orderEntity.setStatus(OrderStatus.COMPLETED);
                orderEntity.setCompletedDateTime(currentTime);
                orderEntity.setExpectedDeliveryDateTime(null);
            }

            orderRepository.saveOrdersEntity(orderEntity);
        }
    }

    private BigDecimal addFoodToOrderRequestAndCalculateTotalPrice(
            List<Integer> foodIds,
            BigDecimal totalPrice,
            Map<FoodEntity, Integer> orderRequest,
            List<Integer> notNullQuantity,
            int index
    ) {

        for (int id : foodIds) {
            FoodEntity food = foodRepository.findFoodEntityById(id);
            Integer value = notNullQuantity.get(index);
            orderRequest.put(food, value);
            totalPrice = calculateTotalPrice(totalPrice, food, value);

            if (foodIds.size() > index) {
                index++;
            }
        }
        return totalPrice;
    }

    private BigDecimal removeOrAdjustOrderRequest(
            Integer quantity,
            Map<FoodEntity, Integer> orderRequest,
            BigDecimal totalPrice,
            Iterator<Map.Entry<FoodEntity, Integer>> iterator,
            Integer value,
            FoodEntity food
    ) {
        if (value > quantity) {
            value = value - quantity;
            orderRequest.replace(food, value);
            return calculateTotalPrice(totalPrice, food, value);
        } else {
            iterator.remove();
            return totalPrice;
        }
    }

    private List<Integer> getNotNullQuantity(List<Integer> quantity) {
        return quantity.stream()
                .filter(Objects::nonNull)
                .toList();
    }

    private BigDecimal calculateTotalPrice(BigDecimal totalPrice, FoodEntity food, Integer quantityValue) {
        BigDecimal sumPrice = food.getPrice().multiply(BigDecimal.valueOf(quantityValue));
        totalPrice = totalPrice.add(sumPrice);
        return totalPrice;
    }

    private Integer generateRandomNumbers() {
        Random random = new Random();
        StringBuilder stringNumber = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int nextInt = random.nextInt(0, 9);
            stringNumber.append(nextInt);
        }
        return Integer.parseInt(stringNumber.toString());
    }
}
