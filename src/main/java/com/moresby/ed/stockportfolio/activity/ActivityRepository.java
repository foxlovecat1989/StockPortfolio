package com.moresby.ed.stockportfolio.activity;

import com.moresby.ed.stockportfolio.activity.Activity;
import org.springframework.data.repository.CrudRepository;

public interface ActivityRepository extends CrudRepository<Activity, Long> {
}
