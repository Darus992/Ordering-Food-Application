package pl.dariuszgilewicz.business;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.dariuszgilewicz.infrastructure.database.entity.CustomerEntity;
import pl.dariuszgilewicz.infrastructure.database.entity.OrdersEntity;
import pl.dariuszgilewicz.infrastructure.database.entity.RestaurantEntity;
import pl.dariuszgilewicz.infrastructure.database.repository.CustomerRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.FoodRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.OrderRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.RestaurantRepository;
import pl.dariuszgilewicz.infrastructure.security.User;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static pl.dariuszgilewicz.util.CustomerFixtures.someCustomerEntity1;
import static pl.dariuszgilewicz.util.FoodFixtures.someFoodsEntityMap1;
import static pl.dariuszgilewicz.util.OrdersFixtures.someOrdersEntity1;
import static pl.dariuszgilewicz.util.RestaurantFixtures.someRestaurantEntity3;
import static pl.dariuszgilewicz.util.UsersFixtures.someCustomerUserModel1;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;
    @Mock
    private FoodRepository foodRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private RestaurantRepository restaurantRepository;

    @Test
    void createOrderAndReturnOrderNumber_shouldWorkSuccessfully() {
        //  given
        Integer[] foodsId = {4, 2, 3};
        Integer[] foodsValue = {1, 2, 3};
        BigDecimal totalPrice = BigDecimal.valueOf(153.5);
        String orderNotes = "";
        User user = someCustomerUserModel1();
        String restaurantEmail = "karczma@restauracja.pl";

        OrdersEntity expectedOrdersEntity = someOrdersEntity1();
        CustomerEntity customerEntity = someCustomerEntity1();
        RestaurantEntity restaurantEntity = someRestaurantEntity3();
        expectedOrdersEntity.setOrderFoods(someFoodsEntityMap1());

        when(customerRepository.findCustomerEntityByPhone(user.getCustomer().getPhone())).thenReturn(customerEntity);
        when(restaurantRepository.findRestaurantEntityByEmail(restaurantEmail)).thenReturn(restaurantEntity);
        when(foodRepository.findFoodEntityById(foodsId[0])).thenReturn(restaurantEntity.getFoodMenu().getFoods().get(0));
        when(foodRepository.findFoodEntityById(foodsId[1])).thenReturn(restaurantEntity.getFoodMenu().getFoods().get(1));
        when(foodRepository.findFoodEntityById(foodsId[2])).thenReturn(restaurantEntity.getFoodMenu().getFoods().get(2));

        //  when
        Integer resultOrderNumber = orderService.createOrderAndReturnOrderNumber(foodsId, foodsValue, totalPrice, orderNotes, user, restaurantEmail);
        expectedOrdersEntity.setOrderNumber(resultOrderNumber);

        //  then
        ArgumentCaptor<OrdersEntity> ordersEntityArgumentCaptor = ArgumentCaptor.forClass(OrdersEntity.class);

        verify(orderRepository, times(1)).saveOrdersEntity(ordersEntityArgumentCaptor.capture());
        OrdersEntity captorValue = ordersEntityArgumentCaptor.getValue();
        captorValue.setReceivedDateTime(expectedOrdersEntity.getReceivedDateTime());

        assertEquals(expectedOrdersEntity, captorValue);
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    void updateOrderStatus_shouldWorkSuccessfully(String status) {
        //  given
        OrdersEntity ordersEntity = someOrdersEntity1();
        Integer orderNumber = 12345;

        when(orderRepository.findOrderEntityByOrderNumber(orderNumber)).thenReturn(ordersEntity);

        //  when
        orderService.updateOrderStatus(orderNumber, status);

        //  then
        if (status.equals("COMPLETED")) {
            assertNotNull(ordersEntity.getCompletedDateTime());
        } else {
            assertNull(ordersEntity.getCompletedDateTime());
        }

        ArgumentCaptor<OrdersEntity> ordersEntityArgumentCaptor = ArgumentCaptor.forClass(OrdersEntity.class);
        verify(orderRepository, times(1)).saveOrdersEntity(ordersEntityArgumentCaptor.capture());

        assertEquals(ordersEntity, ordersEntityArgumentCaptor.getValue());
        assertEquals(ordersEntity.getStatus().name(), status);
    }

    private static Stream<Arguments> provideTestData() {
        return Stream.of(
                Arguments.of("ON_THE_WAY"),
                Arguments.of("COMPLETED")
        );
    }
}