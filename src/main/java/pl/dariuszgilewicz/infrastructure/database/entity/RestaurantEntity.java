package pl.dariuszgilewicz.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "restaurant")
public class RestaurantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "restaurant_id")
    private Integer restaurantId;

    @Column(name = "restaurant_name")
    private String restaurantName;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email", unique = true)
    private String email;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "food_menu_id")
    private FoodMenuEntity foodMenu;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "restaurant_address_id")
    private AddressEntity restaurantAddress;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "restaurant_owner_id")
    private RestaurantOwnerEntity restaurantOwner;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "restaurant_opening_time_id")
    private RestaurantOpeningTimeEntity restaurantOpeningTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_address_id")
    private AddressEntity deliveryAddress;

    @OneToMany(mappedBy = "restaurant")
    private List<OrdersEntity> customerOrders;
}
