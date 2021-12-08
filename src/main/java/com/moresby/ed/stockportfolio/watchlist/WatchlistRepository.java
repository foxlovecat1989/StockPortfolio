package com.moresby.ed.stockportfolio.watchlist;

import com.moresby.ed.stockportfolio.watchlist.WatchlistId;
import com.moresby.ed.stockportfolio.watchlist.Watchlist;
import org.springframework.data.repository.CrudRepository;

public interface WatchlistRepository extends CrudRepository<Watchlist, WatchlistId> {
}
