package pl.dariuszgilewicz.infrastructure.model;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    private String addressCity;
    private String addressDistrict;
    private String addressPostalCode;
    private String addressAddressStreet;
}
