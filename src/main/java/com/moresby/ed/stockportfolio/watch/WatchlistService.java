package com.moresby.ed.stockportfolio.watch;

import java.util.Optional;

public interface WatchlistService {
    Watchlist createWatch(Watchlist watchlist);
    Optional<Watchlist> findOneById(Long id);
    Iterable<Watchlist> findAllByUserId(Long userId);
    Iterable<Watchlist> findAll();
    Watchlist updateWatch(Watchlist watchlist);
    void deleteById(Long id);
}
