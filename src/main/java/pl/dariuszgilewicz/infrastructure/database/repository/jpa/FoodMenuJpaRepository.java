package pl.dariuszgilewicz.infrastructure.database.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.dariuszgilewicz.infrastructure.database.entity.FoodMenuEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface FoodMenuJpaRepository extends JpaRepository<FoodMenuEntity, Integer> {

    @Query("""
            SELECT fm FROM FoodMenuEntity fm
            JOIN fm.foods f
            WHERE f.category = :categoryName
            """)
    Optional<List<FoodMenuEntity>> findAllByCategory(@Param("categoryName") String categoryName);
}
