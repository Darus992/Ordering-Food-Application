package pl.dariuszgilewicz.infrastructure.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    @NotEmpty(message = "City is required.")
    private String city;

    @NotEmpty(message = "District is required.")
    private String district;

    @NotEmpty(message = "Postal Code is required.")
    private String postalCode;

    //  TODO:   DODAĆ SPRAWDZENIE CZY POSIADA NR.
    @NotEmpty(message = "Address street is required.")
    private String addressStreet;
}
