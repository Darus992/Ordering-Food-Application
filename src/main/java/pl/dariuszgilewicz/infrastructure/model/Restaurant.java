package pl.dariuszgilewicz.infrastructure.model;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Restaurant {

    private String restaurantName;
    private String restaurantPhone;
    private String restaurantEmail;
    private FoodMenu foodMenu;
    private Address restaurantAddress;
    private Schedule restaurantSchedule;
}
