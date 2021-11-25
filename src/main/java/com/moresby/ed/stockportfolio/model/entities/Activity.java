package com.moresby.ed.stockportfolio.model.entities;

import com.moresby.ed.stockportfolio.model.ActivityType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "Activity")
@Table(name = "activity")
@NoArgsConstructor
@Getter
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

    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private List<ActivityTypeCapacity> activityTypeCapacities;

    public Activity(String name, String location) {
        this.name = name;
        this.location = location;
        activityTypeCapacities = new ArrayList<>();
        for(ActivityType activityType: ActivityType.values())
            activityTypeCapacities.add(new ActivityTypeCapacity(activityType, 0));
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setActivityTypeCapacities(List<ActivityTypeCapacity> activityTypeCapacities) {
        this.activityTypeCapacities = activityTypeCapacities;
    }
}
