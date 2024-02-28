package pl.dariuszgilewicz.infrastructure.database.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.dariuszgilewicz.infrastructure.database.entity.AddressEntity;
import pl.dariuszgilewicz.infrastructure.database.entity.FoodMenuEntity;
import pl.dariuszgilewicz.infrastructure.database.entity.RestaurantEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantJpaRepository extends JpaRepository<RestaurantEntity, Long> {
    Optional<RestaurantEntity> findByEmail(String restaurantEmail);

    Optional<List<RestaurantEntity>> findAllByRestaurantName(String restaurantName);

    @Query("""
            SELECT r FROM RestaurantEntity r
            JOIN r.foodMenu fm
            WHERE fm IN :foodMenus
            """)
    Optional<List<RestaurantEntity>> findByFoodMenus(@Param("foodMenus") List<FoodMenuEntity> foodMenus);

    @Query("""
            SELECT r FROM RestaurantEntity r
            JOIN r.restaurantAddress ra
            WHERE ra IN :addressEntities
            """)
    Optional<List<RestaurantEntity>> findAllByAddress(@Param("addressEntities") List<AddressEntity> addressEntities);

    boolean existsByEmail(String restaurantEmail);
}
