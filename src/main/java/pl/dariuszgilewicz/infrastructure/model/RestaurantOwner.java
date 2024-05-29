package pl.dariuszgilewicz.infrastructure.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantOwner {

    @NotEmpty(message = "Name is required.")
    private String name;

    @NotEmpty(message = "Surname is required.")
    private String surname;

    @NotEmpty(message = "Pesel is required.")
    private String pesel;
    private List<Restaurant> restaurants;
}
