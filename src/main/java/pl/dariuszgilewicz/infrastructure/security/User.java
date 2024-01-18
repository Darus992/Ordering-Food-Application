package pl.dariuszgilewicz.infrastructure.security;

import lombok.*;
import pl.dariuszgilewicz.infrastructure.model.Restaurant;
import pl.dariuszgilewicz.infrastructure.model.RestaurantOwner;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private String username;
    private String email;
    private String password;
    private UserRole role;
    private RestaurantOwner restaurantOwner;
//    private Boolean isCompleted;
}
