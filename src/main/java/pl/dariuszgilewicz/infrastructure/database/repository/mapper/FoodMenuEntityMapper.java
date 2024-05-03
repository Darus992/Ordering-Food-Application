package pl.dariuszgilewicz.infrastructure.database.repository.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.dariuszgilewicz.infrastructure.database.entity.FoodMenuEntity;
import pl.dariuszgilewicz.infrastructure.model.FoodMenu;

@Component
@AllArgsConstructor
public class FoodMenuEntityMapper {

    private FoodEntityMapper foodEntityMapper;

    public FoodMenu mapFromEntity(FoodMenuEntity entity) {
        return FoodMenu.builder()
                .foodMenuId(entity.getFoodMenuId())
                .foods(foodEntityMapper.mapFromEntityList(entity.getFoods()))
                .build();
    }

    public FoodMenuEntity mapToEntity(FoodMenu foodMenu) {
        return FoodMenuEntity.builder()
                .foodMenuId(foodMenu.getFoodMenuId())
                .foods(foodEntityMapper.mapToEntityList(foodMenu.getFoods()))
                .build();
    }
}
