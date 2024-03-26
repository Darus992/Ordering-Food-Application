package pl.dariuszgilewicz.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
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

    @JdbcTypeCode(Types.VARBINARY)
//    @JdbcTypeCode(Types.BINARY)
    @Column(name = "food_menu_image")
    private byte[] foodMenuImage;

    @Column(name = "menu_name")
    private String menuName;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "food_menu_food",
            joinColumns = @JoinColumn(name = "food_menu_id"),
            inverseJoinColumns = @JoinColumn(name = "food_id")
    )
    private List<FoodEntity> foods;
}
