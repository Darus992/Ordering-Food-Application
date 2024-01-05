package pl.dariuszgilewicz.infrastructure.security;

import lombok.*;
import pl.dariuszgilewicz.infrastructure.model.Restaurant;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private String userName;
    private String email;
    private String password;
    private UserRole role;
    private List<Restaurant> restaurants;
}
