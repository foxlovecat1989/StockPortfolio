package com.moresby.ed.stockportfolio.watch;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/watchs")
@RequiredArgsConstructor
public class WatchlistController {

    private final WatchlistService watchlistService;

    @GetMapping(path = "/findAll", produces = "application/json")
    public Iterable<Watchlist> findAll(){
        return watchlistService.findAll();
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<Watchlist> findOneById(@PathVariable Long id){
        Optional<Watchlist> optWatch = watchlistService.findOneById(id);
        if (optWatch.isEmpty())
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(optWatch.get(), HttpStatus.OK);
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Watchlist createWatch(@RequestBody Watchlist watchlist){
        return watchlistService.createWatch(watchlist);
    }

    @PatchMapping(consumes = "application/json")
    public Watchlist updateWatch(@RequestBody Watchlist watchlist){
        return  watchlistService.updateWatch(watchlist);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id){
        try{
            watchlistService.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
        }
    }
}
