package pl.dariuszgilewicz.infrastructure.model;

import lombok.*;
import pl.dariuszgilewicz.infrastructure.database.enums.OrderStatus;

import java.math.BigDecimal;
import java.util.Map;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Orders {

    private Integer orderNumber;
    private OrderStatus status;
    private String orderNotes;
    private String receivedDateTime;
    private String completedDateTime;
    private Map<Food, Integer> foods;
    private BigDecimal totalPrice;
    private Customer customer;
    private Restaurant restaurant;
}
