package com.moresby.ed.stockportfolio.model.entities;

import com.moresby.ed.stockportfolio.model.ActivityType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "ActivityTypeCapacity")
@Table(name = "activity_type_capacity")
@NoArgsConstructor
@Getter
@Setter
public class ActivityTypeCapacity {

    @Id
    @SequenceGenerator(
            name = "activity_type_capacity_sequence",
            sequenceName = "activity_type_capacity_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "activity_type_capacity_sequence"
    )
    private Long id;

    @Column(
            name = "activity_type",
            nullable = false
    )
    private ActivityType activityType;

    @Column(
            name = "capacities",
            nullable = false
    )
    private Integer capacity;

    public ActivityTypeCapacity(ActivityType activityType, Integer capacity) {
        this.activityType = activityType;
        this.capacity = capacity;
    }
}
