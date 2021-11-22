package com.moresby.ed.stockportfolio.Watchlist;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping(path = "api/v1/watchlists")
@RequiredArgsConstructor
public class WatchlistController {

    private final WatchlistService watchlistService;

    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<Watchlist> findOneById(@PathVariable("id") WatchlistId watchlistId){
        Optional<Watchlist> optWatchlist = watchlistService.findOneById(watchlistId);
        if (optWatchlist.isEmpty())
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(optWatchlist.get(),HttpStatus.OK);
    }

    @GetMapping(path = "/findAll", produces = "application/json")
    public Iterable<Watchlist> findAll(){
        return watchlistService.findAll();
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Watchlist createOne(@RequestBody Watchlist watchlist){
        return watchlistService.createOne(watchlist);
    }

    @PatchMapping(consumes = "application/json")
    public Watchlist refreshDateOfWatchlistStock(@RequestBody Watchlist watchlist){
        return watchlistService.refreshLastUpdateDate(watchlist);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable("id") WatchlistId watchlistId){
        try{
            watchlistService.deleteById(watchlistId);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
        }
    }
}
