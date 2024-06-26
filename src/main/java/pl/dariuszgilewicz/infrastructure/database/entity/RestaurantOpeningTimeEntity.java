package pl.dariuszgilewicz.infrastructure.database.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "restaurant_opening_time")
public class RestaurantOpeningTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "restaurant_opening_time_id")
    private Integer restaurantOpeningTimeId;

    @Column(name = "opening_hour")
    @JsonFormat(pattern = "HH:mm:ss.SSS", shape = JsonFormat.Shape.STRING)
    private LocalTime openingHour;

    @Column(name = "close_hour")
    @JsonFormat(pattern = "HH:mm:ss.SSS", shape = JsonFormat.Shape.STRING)
    private LocalTime closeHour;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week_from")
    private DayOfWeek dayOfWeekFrom;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week_till")
    private DayOfWeek dayOfWeekTill;
}
