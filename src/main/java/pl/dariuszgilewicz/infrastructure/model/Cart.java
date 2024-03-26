package pl.dariuszgilewicz.infrastructure.model;

import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Cart {

    private Integer orderNumber;
    private String foodCategory;
    private String foodName;
    private String foodDescription;
    private BigDecimal foodPrice;
    private Integer quantity;
    private BigDecimal totalPrice;
}
