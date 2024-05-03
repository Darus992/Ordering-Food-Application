package pl.dariuszgilewicz.infrastructure.database.repository.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.dariuszgilewicz.infrastructure.database.entity.FoodEntity;
import pl.dariuszgilewicz.infrastructure.model.Food;
import pl.dariuszgilewicz.infrastructure.util.ImageConverter;

import java.util.List;

@Component
@AllArgsConstructor
public class FoodEntityMapper {
    public Food mapFromEntity(FoodEntity entity) {
        return Food.builder()
                .foodId(entity.getFoodId())
                .foodImage(ImageConverter.convertFromBytes(entity.getFoodImage()))
                .category(entity.getCategory())
                .name(entity.getName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .build();
    }

    public FoodEntity mapToEntity(Food food) {
        FoodEntity foodEntity = FoodEntity.builder()
                .foodId(food.getFoodId())
                .category(food.getCategory())
                .name(food.getName())
                .description(food.getDescription())
                .price(food.getPrice())
                .build();

        if (food.getFoodImage() == null) {
            foodEntity.setFoodImage(ImageConverter.convertFileToBytes(food.getFileImageToUpload()));
        } else {
            foodEntity.setFoodImage(ImageConverter.convertToBytes(food.getFoodImage()));
        }
        return foodEntity;
    }

    public List<Food> mapFromEntityList(List<FoodEntity> entities) {
        return entities.stream()
                .map(this::mapFromEntity)
                .toList();
    }

    public List<FoodEntity> mapToEntityList(List<Food> foods) {
        return foods.stream()
                .map(this::mapToEntity)
                .toList();
    }
}
