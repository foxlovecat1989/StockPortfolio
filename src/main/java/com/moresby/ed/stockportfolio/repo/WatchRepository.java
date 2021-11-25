package com.moresby.ed.stockportfolio.repo;

import com.moresby.ed.stockportfolio.model.entities.Watch;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface WatchRepository extends CrudRepository<Watch, Long> {
    @Query("SELECT w FROM Watch w WHERE w.user.id = ?1")
    Iterable<Watch> findAllByUserIdEqual(Long userId);
}
