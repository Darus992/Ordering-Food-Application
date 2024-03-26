package pl.dariuszgilewicz.infrastructure.database.repository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.dariuszgilewicz.infrastructure.database.entity.FoodEntity;
import pl.dariuszgilewicz.infrastructure.database.repository.jpa.FoodJpaRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.mapper.FoodEntityMapper;
import pl.dariuszgilewicz.infrastructure.database.repository.mapper.FoodMenuEntityMapper;
import pl.dariuszgilewicz.infrastructure.model.Food;

@Repository
@AllArgsConstructor
public class FoodRepository {

    private FoodJpaRepository foodJpaRepository;
    private FoodEntityMapper foodEntityMapper;


    public FoodEntity createAndReturnFoodEntity(Food food) {
        FoodEntity toSave = foodEntityMapper.mapToEntity(food);
        foodJpaRepository.save(toSave);
        return toSave;
    }

    public FoodEntity findFoodEntityById(int id){
        return foodJpaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "FoodEntity with id: [%s] not found".formatted(id)
                ));
    }
}
