package pl.dariuszgilewicz.infrastructure.model;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    private String name;
    private String surname;
    private String phone;
    private Address address;
}
