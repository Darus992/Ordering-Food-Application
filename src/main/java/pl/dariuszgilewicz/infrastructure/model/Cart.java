package pl.dariuszgilewicz.infrastructure.model;

import lombok.*;

import java.math.BigDecimal;
import java.util.Map;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Cart {

    private Map<Food, Integer> cartRequest;
    private Integer[] foodsIdArray;
    private Integer[] foodsValuesArray;
    private BigDecimal totalPrice;
    private String orderNotes;
}
