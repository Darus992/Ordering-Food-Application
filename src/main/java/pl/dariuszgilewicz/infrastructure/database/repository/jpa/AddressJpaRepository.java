package pl.dariuszgilewicz.infrastructure.database.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.dariuszgilewicz.infrastructure.database.entity.AddressEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressJpaRepository extends JpaRepository<AddressEntity, Integer> {

    @Query("""
            SELECT a FROM AddressEntity a WHERE
            LOWER(a.city) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR
            LOWER(a.address) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
            """)
    Optional<List<AddressEntity>> findBySearchTerm(@Param("searchTerm") String searchTerm);
}
