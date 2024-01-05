package pl.dariuszgilewicz.infrastructure.database.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pl.dariuszgilewicz.infrastructure.database.entity.FoodEntity;
import pl.dariuszgilewicz.infrastructure.database.entity.FoodMenuEntity;
import pl.dariuszgilewicz.infrastructure.model.Food;
import pl.dariuszgilewicz.infrastructure.model.FoodMenu;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FoodMenuEntityMapper {

//    default FoodEntity mapFoodToEntity(Food food){
//        return FoodEntity.builder()
//                .category(food.getCategory())
//                .name(food.getName())
//                .description(food.getDescription())
//                .price(food.getPrice())
//                .build();
//    }
//
//    @Mapping(source = "foodMenuName", target = "menuName")
//    default FoodMenuEntity mapToEntity(FoodMenu foodMenu){
//        return FoodMenuEntity.builder()
//                .menuName(foodMenu.getFoodMenuName())
//                .foods(mapFoodToListEntity(foodMenu.getFoods()))
//                .build();
//    }
//
//    default List<FoodEntity> mapFoodToListEntity(List<Food> foods){
//        List<FoodEntity> foodsEntity = new ArrayList<>();
//        for (Food food : foods) {
//            FoodEntity foodEntity = mapFoodToEntity(food);
//            foodsEntity.add(foodEntity);
//        }
//        return foodsEntity;
//    }

    default FoodMenuEntity mapToEntity(FoodMenu foodMenu){
        FoodEntity foodEntity = FoodEntity.builder()
                .category(foodMenu.getFoodCategory())
                .name(foodMenu.getFoodName())
                .description(foodMenu.getFoodDescription())
                .price(foodMenu.getFoodPrice())
                .build();
        return FoodMenuEntity.builder()
                .menuName(foodMenu.getFoodMenuName())
                .foods(new ArrayList<>(List.of(foodEntity)))
                .build();
    }
}
