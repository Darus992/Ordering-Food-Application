package pl.dariuszgilewicz.infrastructure.database.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.OffsetTime;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
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
//    private OffsetTime openingHour;
    private LocalTime openingHour;

    @Column(name = "close_hour")
    @JsonFormat(pattern = "HH:mm:ss.SSS", shape = JsonFormat.Shape.STRING)
//    private OffsetTime closeHour;
    private LocalTime closeHour;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week")
    private DayOfWeek dayOfWeek;

    @ManyToMany(mappedBy = "restaurantOpeningTimes")
    private List<ScheduleEntity> schedules;
}
