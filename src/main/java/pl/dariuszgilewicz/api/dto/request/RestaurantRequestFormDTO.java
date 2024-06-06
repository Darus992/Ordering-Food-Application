package pl.dariuszgilewicz.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.DayOfWeek;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data transfer object for restaurant form details")
public class RestaurantRequestFormDTO {

    @Schema(description = "Username of the user", example = "john_doe")
    private String username;

    @Schema(description = "Email of the user", example = "john.doe@example.com")
    private String userEmail;

    @Schema(description = "Password of the user")
    private String userPassword;

    @Schema(description = "Restaurant image card file")
    private MultipartFile restaurantImageCard;

    @Schema(description = "Restaurant image header file")
    private MultipartFile restaurantImageHeader;

    @Schema(description = "Restaurant name", example = "Delicious Bites")
    private String restaurantName;

    @Schema(description = "Restaurant phone", example = "123369852")
    private String restaurantPhone;

    @Schema(description = "Restaurant email", example = "info@deliciousbites.com")
    private String restaurantEmail;

    @Schema(description = "Restaurant city", example = "New York")
    private String restaurantAddressCity;

    @Schema(description = "Restaurant district")
    private String restaurantAddressDistrict;

    @Schema(description = "Restaurant postal code", example = "10-001")
    private String restaurantAddressPostalCode;

    @Schema(description = "Restaurant street", example = "Broadway 123")
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
