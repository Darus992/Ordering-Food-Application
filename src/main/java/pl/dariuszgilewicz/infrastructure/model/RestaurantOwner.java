package pl.dariuszgilewicz.infrastructure.model;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantOwner {

    private String name;
    private String surname;
    private String pesel;
}
