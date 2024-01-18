package pl.dariuszgilewicz.infrastructure.database.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.dariuszgilewicz.infrastructure.database.entity.FoodMenuEntity;
import pl.dariuszgilewicz.infrastructure.database.repository.jpa.FoodMenuJpaRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.mapper.FoodMenuEntityMapper;
import pl.dariuszgilewicz.infrastructure.model.FoodMenu;

@Repository
@AllArgsConstructor
public class FoodMenuRepository {

    private FoodMenuJpaRepository foodMenuJpaRepository;
    private FoodMenuEntityMapper foodMenuEntityMapper;

    public FoodMenuEntity createAndReturnFoodMenuEntity(FoodMenu foodMenu) {
        FoodMenuEntity toSave = foodMenuEntityMapper.mapToEntity(foodMenu);
        foodMenuJpaRepository.save(toSave);
        return toSave;
    }
}
