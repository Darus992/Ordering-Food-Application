package pl.dariuszgilewicz.api.dto.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.dariuszgilewicz.api.dto.OrderDTO;
import pl.dariuszgilewicz.api.dto.request.OrderFoodRequestDTO;
import pl.dariuszgilewicz.infrastructure.model.Food;
import pl.dariuszgilewicz.infrastructure.model.Orders;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class OrderMapper {

    public final String BASE_URL = "http://localhost:8190/ordering-food-application";
    private final FoodMapper foodMapper;


    public OrderDTO mapToDTO(Orders order) {
        return OrderDTO.builder()
                .orderNumber(order.getOrderNumber().toString())
                .orderStatus(order.getStatus().toString())
                .orderNotes(order.getOrderNotes())
                .orderReceivedDateTime(order.getReceivedDateTime())
                .orderCompletedDateTime(order.getCompletedDateTime())
                .orderFoodRequests(mapOrderRequestToDTO(order.getFoods()))
                .orderTotalPrice(order.getTotalPrice().toString())
                .customerName(order.getCustomer().getName())
                .customerSurname(order.getCustomer().getSurname())
                .customerPhone(order.getCustomer().getPhone())
                .customerAddressCity(order.getCustomer().getAddress().getCity())
                .customerAddressDistrict(order.getCustomer().getAddress().getDistrict())
                .customerAddressPostalCode(order.getCustomer().getAddress().getPostalCode())
                .customerAddressStreet(order.getCustomer().getAddress().getAddressStreet())
                .build();
    }

    public List<OrderDTO> mapToDTOList(List<Orders> orders) {
        return orders.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private List<OrderFoodRequestDTO> mapOrderRequestToDTO(Map<Food, Integer> map) {
        return map.entrySet().stream()
                .map(entry -> new OrderFoodRequestDTO(
                        foodMapper.mapToDTO(entry.getKey(), BASE_URL),
                        entry.getValue().toString()))
                .toList();
    }
}
