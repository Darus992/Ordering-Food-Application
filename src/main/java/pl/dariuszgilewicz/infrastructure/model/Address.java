package pl.dariuszgilewicz.infrastructure.model;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    private String city;
    private String district;
    private String postalCode;
    private String addressStreet;
}
