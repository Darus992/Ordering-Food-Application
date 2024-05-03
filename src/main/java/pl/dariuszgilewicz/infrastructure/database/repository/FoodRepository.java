package pl.dariuszgilewicz.infrastructure.database.repository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.dariuszgilewicz.infrastructure.database.entity.FoodEntity;
import pl.dariuszgilewicz.infrastructure.database.repository.jpa.FoodJpaRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.mapper.FoodEntityMapper;

@Repository
@AllArgsConstructor
public class FoodRepository {

    private FoodJpaRepository foodJpaRepository;


    public FoodEntity saveAndReturn(FoodEntity entity) {
        foodJpaRepository.save(entity);
        return entity;
    }

    public FoodEntity findFoodEntityById(int id){
        return foodJpaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "FoodEntity with id: [%s] not found".formatted(id)
                ));
    }
}
