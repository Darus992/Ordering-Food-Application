package pl.dariuszgilewicz.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import pl.dariuszgilewicz.api.dto.FoodDTO;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data transfer object representing food request")
public class OrderFoodRequestDTO {

    @Schema(description = "Data transfer object representing a food")
    private FoodDTO food;

    @Schema(description = "Amount of the above-mentioned food", example = "2")
    private String quantity;
}
