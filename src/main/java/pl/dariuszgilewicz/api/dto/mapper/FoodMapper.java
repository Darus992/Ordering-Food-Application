package pl.dariuszgilewicz.api.dto.mapper;

import org.springframework.stereotype.Component;
import pl.dariuszgilewicz.api.dto.FoodDTO;
import pl.dariuszgilewicz.api.dto.request.FoodRequestFormDTO;
import pl.dariuszgilewicz.infrastructure.model.Food;

import java.math.BigDecimal;

@Component
public class FoodMapper {

    public FoodDTO mapToDTO(Food food, String baseUrl) {
        return FoodDTO.builder()
                .foodImage(baseUrl + "/image/food/" + food.getFoodId())
                .foodCategory(food.getCategory())
                .foodName(food.getName())
                .foodDescription(food.getDescription())
                .foodPrice(food.getPrice().toString())
                .build();
    }

    public Food mapFromFoodRequestFormDTO(FoodRequestFormDTO dto) {
        return Food.builder()
                .category(dto.getFoodCategory())
                .name(dto.getFoodName())
                .description(dto.getFoodDescription())
                .price(new BigDecimal(dto.getFoodPrice()))
                .fileImageToUpload(dto.getFileImageToUpload())
                .build();
    }
}
