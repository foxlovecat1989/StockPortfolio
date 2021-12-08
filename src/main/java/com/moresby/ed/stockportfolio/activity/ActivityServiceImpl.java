package com.moresby.ed.stockportfolio.activity;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService{
    private final ActivityRepository activityRepository;

    @Override
    public Iterable<Activity> findAll() {
        return activityRepository.findAll();
    }

    @Override
    public Optional<Activity> findById(Long id) {
        return activityRepository.findById(id);
    }

    @Override
    public Activity create(Activity activity) {

        return activityRepository.save(activity);
    }

    @Override
    public Activity update(Activity activity) {

        Optional<Activity> optActivity = activityRepository.findById(activity.getId());
        Activity updateActivity = optActivity.orElseThrow(
                () -> new IllegalStateException(String.format("Activity Id: %s NOT FOUND", activity.getId()))
        );
        updateActivity.setName(
                activity.getName() != null? activity.getName() : updateActivity.getName()
        );
        updateActivity.setLocation(
                activity.getLocation() != null? activity.getLocation() : updateActivity.getLocation()
        );
        updateActivity.setStartDate(
                activity.getStartDate() != null ?
                        activity.getStartDate() : updateActivity.getStartDate()
        );

        updateActivity.setStartTime(
                activity.getStartTime() != null ?
                        activity.getStartTime() : updateActivity.getStartTime()
        );
        updateActivity.setEndTime(activity.getEndTime() != null ?
                activity.getEndTime() : updateActivity.getEndTime()
        );

        updateActivity.setActivityType(
                activity.getActivityType() != null ? activity.getActivityType() : updateActivity.getActivityType()
        );
        updateActivity.setLimitAmount(
                activity.getLimitAmount() != null ? activity.getLimitAmount() : updateActivity.getLimitAmount()
        );

        return activityRepository.save(activity);
    }

    @Override
    public void deleteById(Long id) {
        activityRepository.deleteById(id);
    }
}
