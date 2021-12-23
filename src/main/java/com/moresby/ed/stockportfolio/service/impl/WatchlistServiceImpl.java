package com.moresby.ed.stockportfolio.service.impl;

import com.moresby.ed.stockportfolio.domain.TStock;
import com.moresby.ed.stockportfolio.domain.Watchlist;
import com.moresby.ed.stockportfolio.exception.domain.stock.StockNotfoundException;
import com.moresby.ed.stockportfolio.exception.domain.user.UserNotFoundException;
import com.moresby.ed.stockportfolio.exception.domain.watchlist.WachlistNotFoundException;
import com.moresby.ed.stockportfolio.repository.WatchlistRepository;
import com.moresby.ed.stockportfolio.service.TStockService;
import com.moresby.ed.stockportfolio.service.UserService;
import com.moresby.ed.stockportfolio.service.WatchlistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.moresby.ed.stockportfolio.constant.WatchlistImplConstant.NO_WATCHLIST_FOUND_BY_ID;

@Service
@RequiredArgsConstructor
@Slf4j
public class WatchlistServiceImpl implements WatchlistService {

    private final WatchlistRepository watchlistRepository;
    private final TStockService tStockService;
    private final UserService userService;

    @Override
    public Watchlist findExistWatchlistById(Long watchlistId) throws WachlistNotFoundException {
        return watchlistRepository.findById(watchlistId)
                .orElseThrow(
                        () -> {
                            var errorMsg = String.format(NO_WATCHLIST_FOUND_BY_ID, watchlistId);
                            log.error(errorMsg);
                            return new WachlistNotFoundException(errorMsg);
                        }
                );
    }

    @Override
    public List<Watchlist> findAllByUserId(Long userId) {
        return watchlistRepository.findAllByUserId(userId);
    }

    @Override
    public List<Watchlist> findAll() {
        return watchlistRepository.findAll();
    }

    @Override
    public Watchlist createWatch(String name, Long userId) throws UserNotFoundException {
        var user = userService.findExistingUserById(userId);
        var watchlist =
                Watchlist.builder()
                        .name(name)
                        .user(user)
                        .lastUpdateAt(LocalDateTime.now())
                        .build();

        return watchlistRepository.save(watchlist);
    }

    @Override
    public Watchlist create(Watchlist watchlist){
        return watchlistRepository.save(watchlist);
    }

    @Override
    public Watchlist updateWatchlistName(Long watchlistId, String watchlistName) throws WachlistNotFoundException {
        var originWatch = findExistWatchlistById(watchlistId);
        originWatch.setName(watchlistName);

        return watchlistRepository.save(originWatch);
    }

    @Override
    public Watchlist addStockToWatchlist(TStock tStock, Long watchlistId)
            throws StockNotfoundException, WachlistNotFoundException {
        var watchlist = findExistWatchlistById(watchlistId);
        var stock = tStockService.findExistingStockBySymbol(tStock.getSymbol());
        var stocks = watchlist.getTStocks();
        Set<TStock> setOfStocks = new HashSet<>(stocks);
        setOfStocks.add(stock);
        stocks = new ArrayList<>(setOfStocks);
        watchlist.setTStocks(stocks);

        return watchlistRepository.save(watchlist);
    }

    @Override
    public Watchlist removeStockToWatchlist(TStock tStock, Long watchlistId)
            throws WachlistNotFoundException, StockNotfoundException {
        var watchlist = findExistWatchlistById(watchlistId);
        var stock = tStockService.findExistingStockBySymbol(tStock.getSymbol());
        var stocks = watchlist.getTStocks();
        stocks.remove(stock);
        watchlist.setTStocks(stocks);

        return watchlistRepository.save(watchlist);
    }


    @Override
    public Watchlist updateWatchlistStocks(Watchlist watchlist) throws WachlistNotFoundException {
        var updateWatchlist = findExistWatchlistById(watchlist.getId());
        updateWatchlist.setTStocks(watchlist.getTStocks() != null ? watchlist.getTStocks() : updateWatchlist.getTStocks());

        return watchlistRepository.save(updateWatchlist);
    }

    @Override
    public void deleteById(Long watchlistId) throws WachlistNotFoundException {
        var watchlist = findExistWatchlistById(watchlistId);
        watchlistRepository.delete(watchlist);
    }
}
