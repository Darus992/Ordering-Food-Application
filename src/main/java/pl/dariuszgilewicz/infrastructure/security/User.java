package pl.dariuszgilewicz.infrastructure.security;

import lombok.*;
import pl.dariuszgilewicz.infrastructure.model.Customer;
import pl.dariuszgilewicz.infrastructure.model.RestaurantOwner;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private String username;
    private String email;
    private String password;
    private UserRole role;
    private RestaurantOwner owner;
    private Customer customer;
}
