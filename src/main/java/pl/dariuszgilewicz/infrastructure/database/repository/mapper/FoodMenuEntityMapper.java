package pl.dariuszgilewicz.infrastructure.database.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pl.dariuszgilewicz.infrastructure.database.entity.FoodEntity;
import pl.dariuszgilewicz.infrastructure.database.entity.FoodMenuEntity;
import pl.dariuszgilewicz.infrastructure.model.FoodMenu;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FoodMenuEntityMapper {

    default FoodMenuEntity mapToEntity(FoodMenu foodMenu){
        FoodEntity foodEntity = FoodEntity.builder()
                .category(foodMenu.getFoodCategory())
                .name(foodMenu.getFoodName())
                .description(foodMenu.getFoodDescription())
                .price(foodMenu.getFoodPrice())
                .build();
        return FoodMenuEntity.builder()
                .foodMenuImage(foodMenu.getFoodMenuImage())
                .menuName(foodMenu.getFoodMenuName())
                .foods(new ArrayList<>(List.of(foodEntity)))
                .build();
    }
}
