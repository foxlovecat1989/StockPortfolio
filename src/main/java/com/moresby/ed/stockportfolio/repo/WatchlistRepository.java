package com.moresby.ed.stockportfolio.repo;

import com.moresby.ed.stockportfolio.model.WatchlistId;
import com.moresby.ed.stockportfolio.model.entities.Watchlist;
import org.springframework.data.repository.CrudRepository;

public interface WatchlistRepository extends CrudRepository<Watchlist, WatchlistId> {
}
