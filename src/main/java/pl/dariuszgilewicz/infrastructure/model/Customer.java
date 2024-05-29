package pl.dariuszgilewicz.infrastructure.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @NotEmpty(message = "Name is required.")
    private String name;

    @NotEmpty(message = "Surname is required.")
    private String surname;

    @NotEmpty(message = "Phone is required.")
    private String phone;

    private Address address;
    private List<Orders> customerOrders;
    private boolean isCartEmpty;
}
