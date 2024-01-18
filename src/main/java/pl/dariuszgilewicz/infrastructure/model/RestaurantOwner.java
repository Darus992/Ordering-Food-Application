package pl.dariuszgilewicz.infrastructure.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantOwner {

    private String name;
    private String surname;
    private String pesel;
    private List<Restaurant> restaurants;
}
