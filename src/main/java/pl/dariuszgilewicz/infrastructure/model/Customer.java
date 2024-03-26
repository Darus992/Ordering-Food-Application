package pl.dariuszgilewicz.infrastructure.model;

import lombok.*;

import java.util.List;

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
    private List<Orders> customerOrders;
    private boolean isCartEmpty;
}
