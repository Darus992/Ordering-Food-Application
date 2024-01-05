package pl.dariuszgilewicz.infrastructure.model;

import lombok.*;

import java.time.DayOfWeek;

@Setter
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantOpeningTime {

    private String openingHour;
    private String closeHour;
    private DayOfWeek dayOfWeek;
}
