package pl.dariuszgilewicz.infrastructure.request_form;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.DayOfWeek;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BusinessRequestForm {

    private String username;
    private String userEmail;
    private String userPassword;
    private String ownerName;
    private String ownerSurname;
    private String ownerPesel;
    private MultipartFile restaurantImageCard;
    private String restaurantName;
    private String restaurantPhone;
    private String restaurantEmail;
    private String restaurantAddressCity;
    private String restaurantAddressDistrict;
    private String restaurantAddressPostalCode;
    private String restaurantAddressStreet;
    private String openingHour;
    private String closeHour;
    private DayOfWeek dayOfWeekFrom;
    private DayOfWeek dayOfWeekTill;
}
