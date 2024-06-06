package pl.dariuszgilewicz.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data transfer object for food form")
public class FoodRequestFormDTO {

    @Schema(description = "Food category", example = "Pizza")
    @NotNull(message = "Category is required.")
    private String foodCategory;

    @Schema(description = "Food name", example = "Margherita")
    @NotNull(message = "Name is required.")
    private String foodName;

    @Schema(description = "Description of the food, field is optional", example = "Classic pizza with tomato sauce, mozzarella cheese, and fresh basil.")
    private String foodDescription;

    @Schema(description = "Food price", example = "30.99")
    @NotNull(message = "Price is required.")
    private String foodPrice;

    @Schema(description = "Food image file")
    @NotNull(message = "Image is required.")
    private MultipartFile fileImageToUpload;
}
