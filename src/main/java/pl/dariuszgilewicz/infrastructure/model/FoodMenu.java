package pl.dariuszgilewicz.infrastructure.model;

import lombok.*;

import java.util.List;

@Setter
@Builder
@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class FoodMenu {

    private Integer foodMenuId;
    private String foodMenuName;
    private List<Food> foods;
}
