package pl.dariuszgilewicz.infrastructure.request_form;

import lombok.*;

import java.time.DayOfWeek;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantRequestForm {


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
