package com.moresby.ed.stockportfolio.service;

import com.moresby.ed.stockportfolio.domain.Watchlist;
import com.moresby.ed.stockportfolio.exception.domain.stock.StockNotfoundException;
import com.moresby.ed.stockportfolio.exception.domain.user.UserNotFoundException;
import com.moresby.ed.stockportfolio.exception.domain.watchlist.DuplicatedItemException;
import com.moresby.ed.stockportfolio.exception.domain.watchlist.StockAlreadyExistInTheWatchlistException;
import com.moresby.ed.stockportfolio.exception.domain.watchlist.WatchlistNotFoundException;

import java.util.List;

public interface WatchlistService {
    Watchlist createWatch(String name, String userNumber) throws UserNotFoundException, DuplicatedItemException;
    Watchlist create(Watchlist watchlist);
    Watchlist findExistWatchlistById(Long watchlistId) throws WatchlistNotFoundException;
    List<Watchlist> findAllByUserNumber(String userNumber) throws UserNotFoundException;
    List<Watchlist> findAll();
    Watchlist updateWatchlistName(Long watchlistId, String watchlistName) throws WatchlistNotFoundException;
    Watchlist addStockToWatchlist(String symbol, Long watchlistId) throws StockNotfoundException, WatchlistNotFoundException, StockAlreadyExistInTheWatchlistException;
    void deleteById(Long watchlistId) throws WatchlistNotFoundException;
}
