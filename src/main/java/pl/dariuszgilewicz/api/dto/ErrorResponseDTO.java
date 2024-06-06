package pl.dariuszgilewicz.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Data transfer object for error response details")
public class ErrorResponseDTO {

    @Schema(description = "Error code", example = "404")
    private int statusCode;

    @Schema(description = "Error message", example = "Page not found")
    private String message;

    @Schema(description = "Error details", example = "Page not found. Ensure the provided restaurant email: [restaurant_name@example.com] is correct.")
    private String details;
}
