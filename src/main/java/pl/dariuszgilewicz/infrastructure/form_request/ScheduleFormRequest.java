package pl.dariuszgilewicz.infrastructure.form_request;

import lombok.*;

import java.time.DayOfWeek;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleFormRequest {

    private String openingHour;
    private String closeHour;
    private DayOfWeek dayOfWeek;
}
