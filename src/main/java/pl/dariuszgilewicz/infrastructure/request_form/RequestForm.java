package pl.dariuszgilewicz.infrastructure.request_form;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.DayOfWeek;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class RequestForm {

    @NotEmpty(message = "Username is required.")
    @Size(message = "Username must contain at least 5 characters.", min = 5)
    private String username;

    @NotEmpty(message = "User email is required.")
    private String userEmail;

    @NotEmpty(message = "Password is required.")
    @Size(message = "Password must contain at least 5 characters.", min = 5)
    private String userPassword;

    private MultipartFile restaurantImageCard;
    private MultipartFile restaurantImageHeader;

    @NotEmpty(message = "Restaurant name is required.")
    private String restaurantName;

    @NotEmpty(message = "Restaurant phone is required.")
    @Pattern(regexp = "^\\d+$", message = "Phone number must contain only digits.")
    @Size(message = "The phone number size cannot be larger or smaller than 9.", min = 9, max = 9)
    private String restaurantPhone;

    @NotEmpty(message = "Restaurant email is required.")
    private String restaurantEmail;

    @NotEmpty(message = "City is required.")
    private String restaurantAddressCity;

    @NotEmpty(message = "District is required.")
    private String restaurantAddressDistrict;

    @NotEmpty(message = "Postal code is required.")
    private String restaurantAddressPostalCode;

    @NotEmpty(message = "Street name is required.")
    private String restaurantAddressStreet;

    private String openingHour;
    private String closeHour;
    private DayOfWeek dayOfWeekFrom;
    private DayOfWeek dayOfWeekTill;
}
