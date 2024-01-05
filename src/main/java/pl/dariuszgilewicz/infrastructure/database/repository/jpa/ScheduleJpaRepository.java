package pl.dariuszgilewicz.infrastructure.database.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.dariuszgilewicz.infrastructure.database.entity.ScheduleEntity;

@Repository
public interface ScheduleJpaRepository extends JpaRepository<ScheduleEntity, Long> {
}
