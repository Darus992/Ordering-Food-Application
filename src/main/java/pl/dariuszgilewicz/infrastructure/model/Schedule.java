package pl.dariuszgilewicz.infrastructure.model;

import lombok.*;

import java.time.DayOfWeek;
import java.util.List;

@Setter
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Schedule {

//    private String openingHour;
//    private String closeHour;
//    private DayOfWeek dayOfWeek;
    private List<RestaurantOpeningTime> restaurantOpeningTimes;

    public void addRestaurantOpeningTime(RestaurantOpeningTime time){
        this.restaurantOpeningTimes.add(time);
    }
}
