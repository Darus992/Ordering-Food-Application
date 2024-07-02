package pl.dariuszgilewicz.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.DayOfWeek;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data transfer object for owner user details")
public class BusinessUserDTO extends UserFormDTO {

    @Schema(description = "Name of the owner", example = "John")
    @NotEmpty(message = "Name is required.")
    private String ownerName;

    @Schema(description = "Surname of the owner", example = "Doe")
    @NotEmpty(message = "Surname is required.")
    private String ownerSurname;

    @Schema(description = "Pesel of the owner", example = "91522584927")
    @NotEmpty(message = "Pesel is required.")
    @Size(message = "Pesel number should have 11 numbers.", min = 11, max = 11)
    @Pattern(regexp = "^\\d+$", message = "Pesel must contain only digits.")
    private String ownerPesel;

    @Schema(description = "Image file for the restaurant card")
    private MultipartFile restaurantImageCard;

    @Schema(description = "Image file for the restaurant header")
    private MultipartFile restaurantImageHeader;

    @Schema(description = "Restaurant name", example = "Delicious Bites")
    @NotEmpty(message = "Restaurant name is required.")
    private String restaurantName;

    @Schema(description = "Restaurant phone", example = "123369852")
    @NotEmpty(message = "Restaurant phone is required.")
    @Pattern(regexp = "^\\d+$", message = "Phone number must contain only digits.")
    @Size(message = "The phone number size cannot be larger or smaller than 9.", min = 9, max = 9)
    private String restaurantPhone;

    @Schema(description = "Restaurant email", example = "info@deliciousbites.com")
    @NotEmpty(message = "Restaurant email is required.")
    private String restaurantEmail;

    @Schema(description = "Restaurant city", example = "New York")
    @NotEmpty(message = "City is required.")
    private String restaurantAddressCity;

    @Schema(description = "Restaurant district")
    @NotEmpty(message = "District is required.")
    private String restaurantAddressDistrict;

    @Schema(description = "Restaurant postal code", example = "10-001")
    @NotEmpty(message = "Postal code is required.")
    private String restaurantAddressPostalCode;

    @Schema(description = "Restaurant street", example = "Broadway 123")
    @NotEmpty(message = "Street name is required.")
    private String restaurantAddressStreet;

    @Schema(description = "Restaurant open hour", example = "10:00")
    private String openingHour;

    @Schema(description = "Restaurant close hour", example = "20:00")
    private String closeHour;

    @Schema(description = "Day of the week from which the restaurant is open", example = "MONDAY")
    private DayOfWeek dayOfWeekFrom;

    @Schema(description = "Day of the week until which the restaurant is open", example = "FRIDAY")
    private DayOfWeek dayOfWeekTill;
}
