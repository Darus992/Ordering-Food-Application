package pl.dariuszgilewicz.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data transfer object for customer user details")
public class CustomerUserDTO extends UserFormDTO {

    @Schema(description = "Name of the customer", example = "John")
    @NotEmpty(message = "Name is required.")
    private String customerName;

    @Schema(description = "Surname of the customer", example = "Doe")
    @NotEmpty(message = "Surname is required.")
    private String customerSurname;

    @Schema(description = "Phone of the customer", example = "741852963")
    @NotEmpty(message = "Phone is required.")
    @Pattern(regexp = "^\\d+$", message = "Phone number must contain only digits.")
    @Size(min = 9, max = 9, message = "The phone number size cannot be larger or smaller than 9.")
    private String customerPhone;

    @Schema(description = "Name of the city where the customer lives", example = "Warsaw")
    @NotEmpty(message = "City is required.")
    private String addressCity;

    @Schema(description = "Name of the district in a given city")
    @NotEmpty(message = "District is required.")
    private String addressDistrict;

    @Schema(description = "Postal code for a given city", example = "00-120")
    @NotEmpty(message = "Postal code is required.")
    private String addressPostalCode;

    @Schema(description = "Street name for a given city", example = "Złota 59")
    @NotEmpty(message = "Street is required.")
    private String addressStreet;
}
