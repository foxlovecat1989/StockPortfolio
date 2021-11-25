package com.moresby.ed.stockportfolio.service;

import com.moresby.ed.stockportfolio.model.entities.Activity;
import com.moresby.ed.stockportfolio.repo.ActivityRepository;
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
        updateActivity.setActivityTypeCapacities(
                activity.getActivityTypeCapacities() != null?
                        activity.getActivityTypeCapacities() : updateActivity.getActivityTypeCapacities()
        );

        return activityRepository.save(updateActivity);
    }

    @Override
    public void deleteById(Long id) {
        activityRepository.deleteById(id);
    }
}
