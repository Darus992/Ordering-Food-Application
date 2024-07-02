package pl.dariuszgilewicz.util;

import lombok.experimental.UtilityClass;
import pl.dariuszgilewicz.api.dto.OrderDTO;
import pl.dariuszgilewicz.api.dto.request.OrderFoodRequestDTO;
import pl.dariuszgilewicz.infrastructure.database.entity.OrdersEntity;
import pl.dariuszgilewicz.infrastructure.database.enums.OrderStatus;
import pl.dariuszgilewicz.infrastructure.model.Orders;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import static pl.dariuszgilewicz.util.CustomerFixtures.someCustomerEntity1;
import static pl.dariuszgilewicz.util.CustomerFixtures.someCustomerModel1;
import static pl.dariuszgilewicz.util.FoodFixtures.*;
import static pl.dariuszgilewicz.util.RestaurantFixtures.someRestaurantEntity3;
import static pl.dariuszgilewicz.util.RestaurantFixtures.someRestaurantModel3;

@UtilityClass
public class OrdersFixtures {

    public static List<Integer> someCustomerOrderNumbers1() {
        List<Integer> customerOrderNumbers = new ArrayList<>();
        customerOrderNumbers.add(12345);
        customerOrderNumbers.add(54321);
        customerOrderNumbers.add(11221);
        return customerOrderNumbers;
    }

    public static List<Orders> someOrdersModelList1() {
        List<Orders> ordersList = new ArrayList<>();
        ordersList.add(someOrdersModel1());
        ordersList.add(someOrdersModel2());
        ordersList.add(someOrdersModel3());
        return ordersList;
    }

    public static List<Orders> someOrdersModelList2() {
        List<Orders> ordersList = new ArrayList<>();
        ordersList.add(someOrdersModel1());
        ordersList.add(someOrdersModel2());
        return ordersList;
    }

    public static List<OrderDTO> someOrdersDTOList2() {
        List<OrderDTO> orderDTOList = new ArrayList<>();
        orderDTOList.add(someOrderDTO1());
        orderDTOList.add(someOrderDTO2());
        return orderDTOList;
    }

    public static List<OrdersEntity> someOrdersEntityList2() {
        List<OrdersEntity> ordersList = new ArrayList<>();
        ordersList.add(someOrdersEntity1());
        ordersList.add(someOrdersEntity2());
        return ordersList;
    }

    public static List<OrdersEntity> someOrdersEntityList3() {
        List<OrdersEntity> ordersList = new ArrayList<>();
        ordersList.add(someOrdersEntity1());
        return ordersList;
    }

    public static Orders someOrdersModel1() {
        return Orders.builder()
                .orderNumber(12345)
                .status(OrderStatus.IN_PROGRESS)
                .orderNotes("")
                .receivedDateTime("2024-05-12 12:50:20")
                .completedDateTime("")
                .foods(someFoodsModelMap1())
                .totalPrice(BigDecimal.valueOf(153.5))
                .customer(someCustomerModel1())
                .restaurant(someRestaurantModel3())
                .build();
    }

    public static Orders someOrdersModel2() {
        return Orders.builder()
                .orderNumber(54321)
                .status(OrderStatus.ON_THE_WAY)
                .orderNotes("Tekst pomocniczy")
                .receivedDateTime("2024-05-12 13:30:10")
                .completedDateTime("")
                .foods(someFoodsModelMap1())
                .totalPrice(BigDecimal.valueOf(153.5))
                .customer(someCustomerModel1())
                .restaurant(someRestaurantModel3())
                .build();
    }

    public static Orders someOrdersModel3() {
        return Orders.builder()
                .orderNumber(11221)
                .status(OrderStatus.COMPLETED)
                .orderNotes("Kolejny teks")
                .receivedDateTime("2024-04-10 15:18:27")
                .completedDateTime("2024-04-10 15:50:41")
                .foods(someFoodsModelMap1())
                .totalPrice(BigDecimal.valueOf(153.5))
                .customer(someCustomerModel1())
                .restaurant(someRestaurantModel3())
                .build();
    }

    public static OrdersEntity someOrdersEntity1() {
        return OrdersEntity.builder()
                .orderNumber(12345)
                .status(OrderStatus.IN_PROGRESS)
                .orderNotes("")
                .receivedDateTime(OffsetDateTime.of(2024, 5, 12, 12, 50, 20, 10, ZoneOffset.UTC))
                .orderFoods(someFoodsEntityMap2())
                .totalPrice(BigDecimal.valueOf(153.5))
                .customer(someCustomerEntity1())
                .restaurant(someRestaurantEntity3())
                .build();
    }

    public static OrdersEntity someOrdersEntity2() {
        return OrdersEntity.builder()
                .orderNumber(54321)
                .status(OrderStatus.ON_THE_WAY)
                .orderNotes("Tekst pomocniczy")
                .receivedDateTime(OffsetDateTime.of(2024, 5, 12, 13, 30, 10, 10, ZoneOffset.UTC))
                .orderFoods(someFoodsEntityMap1())
                .totalPrice(BigDecimal.valueOf(153.5))
                .customer(someCustomerEntity1())
                .restaurant(someRestaurantEntity3())
                .build();
    }

    public static OrderDTO someOrderDTO1() {
        return OrderDTO.builder()
                .orderNumber("12345")
                .orderStatus(OrderStatus.IN_PROGRESS.name())
                .orderNotes("")
                .orderReceivedDateTime("2024-05-12 12:50:20")
                .orderCompletedDateTime("")
                .orderFoodRequests(someOrderFoodRequestsDTO1())
                .orderTotalPrice("153.5")
                .customerName("Leszek")
                .customerSurname("Zaradny")
                .customerPhone("154457147")
                .customerAddressCity("Warszawa")
                .customerAddressDistrict("Testowy")
                .customerAddressPostalCode("22-220")
                .customerAddressStreet("Kolejna ulica 100")
                .build();
    }

    public static OrderDTO someOrderDTO2() {
        return OrderDTO.builder()
                .orderNumber("54321")
                .orderStatus(OrderStatus.ON_THE_WAY.name())
                .orderNotes("Tekst pomocniczy")
                .orderReceivedDateTime("2024-05-12 13:30:10")
                .orderCompletedDateTime("")
                .orderFoodRequests(someOrderFoodRequestsDTO1())
                .orderTotalPrice("153.5")
                .customerName("Leszek")
                .customerSurname("Zaradny")
                .customerPhone("154457147")
                .customerAddressCity("Warszawa")
                .customerAddressDistrict("Testowy")
                .customerAddressPostalCode("22-220")
                .customerAddressStreet("Kolejna ulica 100")
                .build();
    }

    public static List<OrderFoodRequestDTO> someOrderFoodRequestsDTO1() {
        List<OrderFoodRequestDTO> result = new ArrayList<>();
        result.add(new OrderFoodRequestDTO(someFoodDTO1(), "1"));
        result.add(new OrderFoodRequestDTO(someFoodDTO2(), "2"));
        result.add(new OrderFoodRequestDTO(someFoodDTO3(), "3"));
        return result;
    }
}
