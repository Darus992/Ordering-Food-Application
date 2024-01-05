package pl.dariuszgilewicz.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.*;
import pl.dariuszgilewicz.infrastructure.security.UserEntity;

import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customer")
public class CustomerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Integer customerId;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "phone", unique = true)
    private String phone;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private AddressEntity address;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "customer")
    private UserEntity user;

    @OneToMany(mappedBy = "customer")
    private List<OrdersEntity> customerOrders;
}
