package pl.dariuszgilewicz.infrastructure.database.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.dariuszgilewicz.infrastructure.database.entity.FoodEntity;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface FoodJpaRepository extends JpaRepository<FoodEntity, Integer> {

    @Query("""
            SELECT e FROM FoodEntity e WHERE e.category = :category
                        AND e.name = :name
                        AND e.description = :description
                        AND e.price = :price
                        AND e.foodImage = :foodImage
            """)
    Optional<FoodEntity> findEntityByHisFields(
            @Param("category") String category,
            @Param("name") String name,
            @Param("description") String description,
            @Param("price") BigDecimal price,
            @Param("foodImage") byte[] foodImage
    );
}
