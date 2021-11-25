package com.moresby.ed.stockportfolio.model.entities;

import com.moresby.ed.stockportfolio.model.ActivityType;
import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


@Entity(name = "Activity")
@Table(name = "activity")
@RequiredArgsConstructor
@Getter
@Setter
public class Activity {

    @Id
    @SequenceGenerator(
            name = "activity_sequence",
            sequenceName = "activity_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "activity_sequence"
    )
    private Long id;

    @Column(
            name = "name",
            nullable = false
    )
    @NotNull(message = "Name cannot be blank.")
    private String name;

    @Column(
            name = "location",
            nullable = false
    )
    @NotNull(message = "Location cannot be blank.")
    private String location;

    @Column(name = "start_date_time")
    private LocalDateTime startDateTime;

    @Column(name = "end_date_time")
    private LocalDateTime endDateTime;

    @Column(name = "activity_type")
    private ActivityType activityType;

    @Column(name = "limit_amount")
    private Integer limitAmount;
}
