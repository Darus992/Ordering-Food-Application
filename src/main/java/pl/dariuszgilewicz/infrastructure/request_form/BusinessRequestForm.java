package pl.dariuszgilewicz.infrastructure.request_form;

import lombok.*;
import pl.dariuszgilewicz.infrastructure.security.UserRole;

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
