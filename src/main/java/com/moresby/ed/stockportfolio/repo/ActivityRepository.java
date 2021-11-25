package com.moresby.ed.stockportfolio.repo;

import com.moresby.ed.stockportfolio.model.entities.Activity;
import org.springframework.data.repository.CrudRepository;

public interface ActivityRepository extends CrudRepository<Activity, Long> {
}
