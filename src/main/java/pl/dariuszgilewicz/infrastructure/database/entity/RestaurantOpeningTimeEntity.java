package pl.dariuszgilewicz.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
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
    private OffsetTime openingHour;

    @Column(name = "close_hour")
    private OffsetTime closeHour;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week")
    private DayOfWeek dayOfWeek;

    @ManyToMany(mappedBy = "restaurantOpeningTimes")
    private List<ScheduleEntity> schedules;
}
