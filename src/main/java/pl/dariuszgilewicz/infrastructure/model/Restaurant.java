package pl.dariuszgilewicz.infrastructure.model;

import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Restaurant {

    private String restaurantImageCard;
    private String restaurantImageHeader;
    private String restaurantName;
    private String restaurantPhone;
    private String restaurantEmail;
    private FoodMenu foodMenu;
    private Address restaurantAddress;
    private RestaurantOwner restaurantOwner;
    private RestaurantOpeningTime restaurantOpeningTime;
    private List<Integer> customerOrdersNumbers;
    private List<Orders> orders;
}
