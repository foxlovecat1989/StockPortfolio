package com.moresby.ed.stockportfolio.service.impl;

import com.moresby.ed.stockportfolio.domain.TStock;
import com.moresby.ed.stockportfolio.domain.Watchlist;
import com.moresby.ed.stockportfolio.exception.domain.stock.StockNotfoundException;
import com.moresby.ed.stockportfolio.exception.domain.user.UserNotFoundException;
import com.moresby.ed.stockportfolio.exception.domain.watchlist.DuplicatedItemException;
import com.moresby.ed.stockportfolio.exception.domain.watchlist.StockAlreadyExistInTheWatchlistException;
import com.moresby.ed.stockportfolio.exception.domain.watchlist.WatchlistNotFoundException;
import com.moresby.ed.stockportfolio.repository.WatchlistRepository;
import com.moresby.ed.stockportfolio.service.TStockService;
import com.moresby.ed.stockportfolio.service.UserService;
import com.moresby.ed.stockportfolio.service.WatchlistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.moresby.ed.stockportfolio.constant.WatchlistImplConstant.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class WatchlistServiceImpl implements WatchlistService {

    private final WatchlistRepository watchlistRepository;
    private final TStockService tStockService;
    private final UserService userService;

    @Override
    public Watchlist findExistWatchlistById(Long watchlistId) throws WatchlistNotFoundException {
        return watchlistRepository.findById(watchlistId)
                .orElseThrow(
                        () -> {
                            var errorMsg = String.format(NO_WATCHLIST_FOUND_BY_ID, watchlistId);
                            log.error(errorMsg);
                            return new WatchlistNotFoundException(errorMsg);
                        }
                );
    }

    @Override
    public List<Watchlist> findAllByUserNumber(String userNumber) throws UserNotFoundException {
        var user = userService.findExistingUserByUserNumber(userNumber);

        return watchlistRepository.findAllByUserId(user.getId());
    }

    @Override
    public List<Watchlist> findAll() {
        return watchlistRepository.findAll();
    }

    @Override
    public Watchlist createWatch(String name, String userNumber) throws UserNotFoundException, DuplicatedItemException {
        var user = userService.findExistingUserByUserNumber(userNumber);
        boolean  isDuplicatedName =
                watchlistRepository.findAllByUserId(user.getId()).stream()
                        .anyMatch(next -> next.getName().equals(name));
        if(isDuplicatedName)
            throw new DuplicatedItemException(String.format(DUPLICATED_WATCHLIST_NAME, name));
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
    public Watchlist updateWatchlistName(Long watchlistId, String watchlistName) throws WatchlistNotFoundException {
        var originWatch = findExistWatchlistById(watchlistId);
        originWatch.setName(watchlistName);

        return watchlistRepository.save(originWatch);
    }

    @Override
    public Watchlist addStockToWatchlist(String symbol, Long watchlistId)
            throws StockNotfoundException, WatchlistNotFoundException, StockAlreadyExistInTheWatchlistException {
        var watchlist = findExistWatchlistById(watchlistId);
        var stock = tStockService.findExistingStockBySymbol(symbol);
        var stocks = watchlist.getTStocks();
        boolean isDuplicated =
                stocks.stream().anyMatch(next -> stock.getSymbol().equals(next.getSymbol()));

        if(isDuplicated)
            throw new StockAlreadyExistInTheWatchlistException(
                    String.format(STOCK_IS_ALREADY_IN_THE_WATCHLIST, stock.getSymbol(), watchlist.getName()));

        Set<TStock> setOfStocks = new HashSet<>(stocks);
        setOfStocks.add(stock);
        stocks = new ArrayList<>(setOfStocks);
        watchlist.setTStocks(stocks);

        return watchlistRepository.save(watchlist);
    }

    @Override
    public Watchlist removeStockToWatchlist(String symbol, Long watchlistId)
            throws WatchlistNotFoundException, StockNotfoundException {
        var watchlist = findExistWatchlistById(watchlistId);
        var stock = tStockService.findExistingStockBySymbol(symbol);
        var newStocks =
                watchlist.getTStocks().stream()
                        .filter(next -> !(next.getSymbol().equals(stock.getSymbol())))
                        .collect(Collectors.toList());
        watchlist.setTStocks(newStocks);

        return watchlistRepository.save(watchlist);
    }

    @Override
    @Transactional
    public void deleteById(Long watchlistId) throws WatchlistNotFoundException {
        var watchlist = findExistWatchlistById(watchlistId);
        watchlistRepository.deleteById(watchlist.getId());
    }
}
