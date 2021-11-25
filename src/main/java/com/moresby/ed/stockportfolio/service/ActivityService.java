package com.moresby.ed.stockportfolio.service;


import com.moresby.ed.stockportfolio.model.entities.Activity;

import java.util.Optional;

public interface ActivityService {
    Iterable<Activity> findAll();
    Optional<Activity> findById(Long id);
    Activity create(Activity activity);
    Activity update(Activity activity);
    void deleteById(Long id);
}
