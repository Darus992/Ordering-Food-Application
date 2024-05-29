package pl.dariuszgilewicz.infrastructure.database.repository.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.dariuszgilewicz.infrastructure.database.entity.OrdersEntity;
import pl.dariuszgilewicz.infrastructure.model.Orders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static pl.dariuszgilewicz.util.FoodFixtures.*;
import static pl.dariuszgilewicz.util.OrdersFixtures.*;

@ExtendWith(MockitoExtension.class)
class OrderEntityMapperTest {

    @InjectMocks
    private OrderEntityMapper orderEntityMapper;

    @Mock
    private FoodEntityMapper foodEntityMapper;
    @Mock
    private AddressEntityMapper addressEntityMapper;


    @Test
    void mapFromEntity_shouldWorkSuccessfully() {
        //  given
        OrdersEntity orderEntity = someOrdersEntity1();
        Orders expectedOrder = someOrdersModel1();

        orderEntity.setOrderFoods(someFoodsEntityMap1());
        expectedOrder.getRestaurant().setFoodMenu(null);
        expectedOrder.getRestaurant().setCustomerOrdersNumbers(null);
        expectedOrder.getRestaurant().setRestaurantOpeningTime(null);

        when(addressEntityMapper.mapFromEntity(orderEntity.getCustomer().getAddress())).thenReturn(expectedOrder.getCustomer().getAddress());
        when(addressEntityMapper.mapFromEntity(orderEntity.getRestaurant().getRestaurantAddress())).thenReturn(expectedOrder.getRestaurant().getRestaurantAddress());
        when(foodEntityMapper.mapFromEntity(someFoodEntity1())).thenReturn(someFoodModel1());
        when(foodEntityMapper.mapFromEntity(someFoodEntity2())).thenReturn(someFoodModel2());
        when(foodEntityMapper.mapFromEntity(someFoodEntity3())).thenReturn(someFoodModel3());

        Orders resultOrder = orderEntityMapper.mapFromEntity(orderEntity);

        //  then
        assertEquals(expectedOrder, resultOrder);
    }

    @Test
    void mapFromEntityList_shouldWorkSuccessfully() {
        //  given
        List<OrdersEntity> ordersEntities = someOrdersEntityList2();
        List<Orders> expectedOrdersList = someOrdersModelList2();

        ordersEntities.get(0).setOrderFoods(someFoodsEntityMap1());
        expectedOrdersList.get(0).getRestaurant().setFoodMenu(null);
        expectedOrdersList.get(0).getRestaurant().setCustomerOrdersNumbers(null);
        expectedOrdersList.get(0).getRestaurant().setRestaurantOpeningTime(null);

        expectedOrdersList.get(1).getRestaurant().setFoodMenu(null);
        expectedOrdersList.get(1).getRestaurant().setCustomerOrdersNumbers(null);
        expectedOrdersList.get(1).getRestaurant().setRestaurantOpeningTime(null);

        for (int i = 0; i < expectedOrdersList.size(); i++) {
            when(addressEntityMapper.mapFromEntity(ordersEntities.get(i).getCustomer().getAddress())).thenReturn(expectedOrdersList.get(i).getCustomer().getAddress());
            when(addressEntityMapper.mapFromEntity(ordersEntities.get(i).getRestaurant().getRestaurantAddress())).thenReturn(expectedOrdersList.get(i).getRestaurant().getRestaurantAddress());
            when(foodEntityMapper.mapFromEntity(someFoodEntity1())).thenReturn(someFoodModel1());
            when(foodEntityMapper.mapFromEntity(someFoodEntity2())).thenReturn(someFoodModel2());
            when(foodEntityMapper.mapFromEntity(someFoodEntity3())).thenReturn(someFoodModel3());
        }

        //  when
        List<Orders> resultOrdersList = orderEntityMapper.mapFromEntityList(ordersEntities);

        //  then
        assertEquals(expectedOrdersList, resultOrdersList);
    }
}