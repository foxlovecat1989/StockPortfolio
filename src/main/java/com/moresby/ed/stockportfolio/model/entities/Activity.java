package com.moresby.ed.stockportfolio.model.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.moresby.ed.stockportfolio.model.ActivityType;
import com.moresby.ed.stockportfolio.model.SqlTimeDeserializer;
import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.sql.Time;

@Entity(name = "Activity")
@Table(name = "activity")
@NoArgsConstructor
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

    @Column(name = "start_date")
    private Date startDate;

    @JsonFormat(pattern = "HH:mm")
    @JsonDeserialize(using = SqlTimeDeserializer.class)
    @Column(name = "start_time")
    private Time startTime;

    @JsonFormat(pattern = "HH:mm")
    @JsonDeserialize(using = SqlTimeDeserializer.class)
    @Column(name = "end_time")
    private Time endTime;

    @Column(name = "activity_type")
    private ActivityType activityType;

    @Column(name = "limit_amount")
    private Integer limitAmount;


    public Integer getDisplayDay() {
        return this.startDate.toLocalDate().getDayOfMonth();
    }

    public String getDisplayMonth() {
        return this.startDate.toLocalDate().getMonth().toString();
    }

    public String getDisplayDayOfWeek() {
        return this.startDate.toLocalDate().getDayOfWeek().toString();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Time getStartTime() {
        return startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    public Integer getLimitAmount() {
        return limitAmount;
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

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }

    public void setLimitAmount(Integer limitAmount) {
        this.limitAmount = limitAmount;
    }
}
