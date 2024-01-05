package pl.dariuszgilewicz.infrastructure.model;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FoodMenu {

    private String foodMenuName;
    private String foodCategory;
    private String foodName;
    private String foodDescription;
    private BigDecimal foodPrice;
    private List<Food> foods;
}
