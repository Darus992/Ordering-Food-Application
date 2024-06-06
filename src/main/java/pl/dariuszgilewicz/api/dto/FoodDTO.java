package pl.dariuszgilewicz.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data transfer object for food details")
public class FoodDTO {

    @Schema(description = "Food image", example = "http://localhost:8190/ordering-food-application/image/food/1")
    private String foodImage;

    @Schema(description = "Food category", example = "Pizza")
    private String foodCategory;

    @Schema(description = "Food name", example = "Margherita")
    private String foodName;

    @Schema(description = "Description of the food, field is optional", example = "Classic pizza with tomato sauce, mozzarella cheese, and fresh basil.")
    private String foodDescription;

    @Schema(description = "Food price", example = "30.99")
    private String foodPrice;
}
