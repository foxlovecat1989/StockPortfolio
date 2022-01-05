package com.moresby.ed.stockportfolio.resource;

import com.moresby.ed.stockportfolio.domain.HttpResponse;
import com.moresby.ed.stockportfolio.domain.Watchlist;
import com.moresby.ed.stockportfolio.exception.domain.stock.StockNotfoundException;
import com.moresby.ed.stockportfolio.exception.domain.user.UserNotFoundException;
import com.moresby.ed.stockportfolio.exception.domain.watchlist.DuplicatedItemException;
import com.moresby.ed.stockportfolio.exception.domain.watchlist.StockAlreadyExistInTheWatchlistException;
import com.moresby.ed.stockportfolio.exception.domain.watchlist.WatchlistNotFoundException;
import com.moresby.ed.stockportfolio.exception.handler.WatchlistExceptionHandling;
import com.moresby.ed.stockportfolio.service.WatchlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.moresby.ed.stockportfolio.constant.WatchlistImplConstant.WATCHLIST_DELETED_SUCCESSFULLY;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/api/v1/watchlist")
@RequiredArgsConstructor
public class WatchlistController extends WatchlistExceptionHandling {

    private final WatchlistService watchlistService;

    @GetMapping(path = "/findAll/{userNumber}", produces = APPLICATION_JSON_VALUE)
    @PreAuthorize(value = "hasAnyAuthority('watchlist:read')")
    public ResponseEntity<List<Watchlist>> findAllByUserNumber(@PathVariable("userNumber") String userNumber)
            throws UserNotFoundException {
        var watchlists = watchlistService.findAllByUserNumber(userNumber);

        return new ResponseEntity<>(watchlists, HttpStatus.OK);
    }

    @PostMapping(path = "/create/{userNumber}", consumes = APPLICATION_JSON_VALUE)
    @PreAuthorize(value = "hasAnyAuthority('watchlist:create')")
    public ResponseEntity<Watchlist> createWatch(
            @PathVariable("userNumber") String userNumber,
            @RequestBody Watchlist watchlist)
            throws UserNotFoundException, DuplicatedItemException {

        var newWatchlist = watchlistService.createWatch(watchlist.getName(), userNumber);

        return new ResponseEntity<>(newWatchlist, HttpStatus.OK);
    }

    @PreAuthorize(value = "hasAnyAuthority('watchlist:create')")
    @PostMapping(path = "/add/{symbol}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Watchlist> addStockToWatchlist(
            @PathVariable("symbol") String symbol,
            @RequestBody Watchlist watchlist)
            throws WatchlistNotFoundException, StockNotfoundException, StockAlreadyExistInTheWatchlistException {
        var resultWatchlist = watchlistService.addStockToWatchlist(symbol, watchlist.getId());

        return new ResponseEntity<>(resultWatchlist, HttpStatus.OK);
    }

    @PreAuthorize(value = "hasAnyAuthority('watchlist:update')")
    @PostMapping(path = "/remove/item/{symbol}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Watchlist> removeStockToWatchlist(
            @PathVariable("symbol") String symbol,
            @RequestBody Watchlist watchlist)
            throws WatchlistNotFoundException, StockNotfoundException {
        var resultWatchlist = watchlistService.removeStockToWatchlist(symbol, watchlist.getId());

        return new ResponseEntity<>(resultWatchlist, HttpStatus.OK);
    }

    @PreAuthorize(value = "hasAnyAuthority('watchlist:update')")
    @PatchMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Watchlist> updateWatchlistName(@RequestBody Watchlist watchlist)
            throws WatchlistNotFoundException {
        var updateWatchlist = watchlistService.updateWatchlistName(watchlist.getId(), watchlist.getName());

        return new ResponseEntity<>(updateWatchlist, HttpStatus.OK);
    }

    @PreAuthorize(value = "hasAnyAuthority('watchlist:delete')")
    @DeleteMapping(path = "/{watchlistId}")
    public ResponseEntity<HttpResponse> deleteById(@PathVariable("watchlistId") Long watchlistId)
            throws WatchlistNotFoundException {
        watchlistService.deleteById(watchlistId);

        return response(HttpStatus.NO_CONTENT, WATCHLIST_DELETED_SUCCESSFULLY);
    }

    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(
                new HttpResponse(
                        httpStatus.value(),
                        httpStatus,
                        httpStatus.getReasonPhrase().toUpperCase(),
                        message),
                httpStatus);
    }
}
