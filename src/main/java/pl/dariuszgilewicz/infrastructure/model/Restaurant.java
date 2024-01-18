package pl.dariuszgilewicz.infrastructure.model;

import lombok.*;

import java.util.List;

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
    private RestaurantOpeningTime restaurantOpeningTime;
//    private RestaurantOwner restaurantOwner;
//    private List<Address> deliveryAddress;
}
