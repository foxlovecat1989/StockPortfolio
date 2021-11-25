package com.moresby.ed.stockportfolio.service;

import com.moresby.ed.stockportfolio.model.entities.Watch;

import java.util.Optional;

public interface WatchService {
    Watch createWatch(Watch watch);
    Optional<Watch> findOneById(Long id);
    Iterable<Watch> findAllByUserId(Long userId);
    Iterable<Watch> findAll();
    Watch updateWatch(Watch watchlist);
    void deleteById(Long id);
}
