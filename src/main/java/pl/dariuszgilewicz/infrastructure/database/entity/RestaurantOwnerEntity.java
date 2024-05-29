package pl.dariuszgilewicz.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import pl.dariuszgilewicz.infrastructure.security.UserEntity;

import java.util.List;

@Getter
@Setter
@Entity
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "restaurant_owner")
public class RestaurantOwnerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "restaurant_owner_id")
    private Integer restaurantOwnerId;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "pesel", unique = true)
    private String pesel;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "owner")
    private UserEntity user;

    @Fetch(FetchMode.JOIN)
    @OneToMany(mappedBy = "restaurantOwner")
    private List<RestaurantEntity> restaurants;
}
