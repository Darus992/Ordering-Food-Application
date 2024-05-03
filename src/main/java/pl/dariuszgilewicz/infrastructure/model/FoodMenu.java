package pl.dariuszgilewicz.infrastructure.model;

import lombok.*;

import java.util.List;

@Setter
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FoodMenu {

    private int foodMenuId;
    private String foodMenuName;
    private List<Food> foods;
}
