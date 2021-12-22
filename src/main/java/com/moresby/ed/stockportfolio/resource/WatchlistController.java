package com.moresby.ed.stockportfolio.resource;

import com.moresby.ed.stockportfolio.domain.HttpResponse;
import com.moresby.ed.stockportfolio.domain.TStock;
import com.moresby.ed.stockportfolio.domain.Watchlist;
import com.moresby.ed.stockportfolio.exception.domain.stock.StockNotfoundException;
import com.moresby.ed.stockportfolio.exception.domain.user.UserNotFoundException;
import com.moresby.ed.stockportfolio.exception.domain.watchlist.WachlistNotFoundException;
import com.moresby.ed.stockportfolio.service.UserService;
import com.moresby.ed.stockportfolio.service.WatchlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.moresby.ed.stockportfolio.constant.WatchlistImplConstant.WATCHLIST_DELETED_SUCCESSFULLY;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/api/v1/watchlist")
@RequiredArgsConstructor
public class WatchlistController {

    private final WatchlistService watchlistService;
    private final UserService userService;

    @GetMapping(path = "/findAll", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Watchlist>> findAll(){
        var watchlists =  watchlistService.findAll();

        return new ResponseEntity<>(watchlists, HttpStatus.OK);
    }

    @GetMapping(path = "/{userNumber}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Watchlist>> findAllByUserNumber(@PathVariable("userNumber") String userNumber)
            throws UserNotFoundException {
        var user = userService.findExistingUserByUserNumber(userNumber);
        var watchlists = watchlistService.findAllByUserId(user.getId());

        return new ResponseEntity<>(watchlists, HttpStatus.OK);
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Watchlist> createWatch(@RequestBody Watchlist watchlist){
        var newWatchlist = watchlistService.createWatch(watchlist);

        return new ResponseEntity<>(newWatchlist, HttpStatus.OK);
    }

    @GetMapping(path = "", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Watchlist> updateWatchlistName(@RequestParam Long watchlistId, @RequestParam String watchlistName)
            throws WachlistNotFoundException {
        var updateWatchlist = watchlistService.updateWatchlistName(watchlistId, watchlistName);

        return new ResponseEntity<>(updateWatchlist, HttpStatus.OK);
    }

    @PatchMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Watchlist> addStockToWatchlist(@RequestBody TStock tStock, @RequestParam Long watchlistId)
            throws WachlistNotFoundException, StockNotfoundException {
        var watchlist = watchlistService.addStockToWatchlist(tStock, watchlistId);

        return new ResponseEntity<>(watchlist, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{userNumber}")
    public ResponseEntity<HttpResponse> deleteById(@PathVariable String userNumber)
            throws UserNotFoundException, WachlistNotFoundException {
        var user = userService.findExistingUserByUserNumber(userNumber);
        watchlistService.deleteById(user.getId());

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
