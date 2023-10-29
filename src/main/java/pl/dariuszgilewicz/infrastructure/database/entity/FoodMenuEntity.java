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
@Table(name = "food_menu")
public class FoodMenuEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "food_menu_id")
    private Integer foodMenuId;

    @Column(name = "menu_name")
    private String menuName;

    @ManyToMany(cascade = CascadeType.ALL)  //nie zapomnieć dodać tabeli łączącej do bazy danych i do migracji
    @JoinTable(
            name = "food_menu_food",
            joinColumns = @JoinColumn(name = "food_menu_id"),
            inverseJoinColumns = @JoinColumn(name = "food_id")
    )
    private List<FoodEntity> foods;
}
