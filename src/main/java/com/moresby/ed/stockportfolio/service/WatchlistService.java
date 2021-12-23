package com.moresby.ed.stockportfolio.service;

import com.moresby.ed.stockportfolio.domain.TStock;
import com.moresby.ed.stockportfolio.domain.Watchlist;
import com.moresby.ed.stockportfolio.exception.domain.stock.StockNotfoundException;
import com.moresby.ed.stockportfolio.exception.domain.user.UserNotFoundException;
import com.moresby.ed.stockportfolio.exception.domain.watchlist.WachlistNotFoundException;

import java.util.List;

public interface WatchlistService {
    Watchlist createWatch(String name, Long userId) throws UserNotFoundException;
    Watchlist create(Watchlist watchlist);
    Watchlist findExistWatchlistById(Long watchlistId) throws WachlistNotFoundException;
    List<Watchlist> findAllByUserId(Long userId);
    List<Watchlist> findAll();
    Watchlist updateWatchlistName(Long watchlistId, String watchlistName) throws WachlistNotFoundException;
    Watchlist addStockToWatchlist(Long stockId, Long watchlistId) throws StockNotfoundException, WachlistNotFoundException;
    void deleteById(Long watchlistId) throws WachlistNotFoundException;
}
