package com.moresby.ed.stockportfolio.watchlist;

import java.util.Optional;

public interface WatchlistService {
    // R
    Optional<Watchlist> findOneById(WatchlistId watchlistId);
    Iterable<Watchlist> findAllByUserId(Long investorId);
    Iterable<Watchlist> findAll();
    // C
    Watchlist createOne(Watchlist watchlist);
    // U
    Watchlist refreshLastUpdateDate(Watchlist watchlist);
    // D
    void deleteById(WatchlistId watchlistId);
}
