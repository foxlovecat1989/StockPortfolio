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
public class WatchController {

    private final WatchService watchService;

    @GetMapping(path = "/findAll", produces = "application/json")
    public Iterable<Watch> findAll(){
        return watchService.findAll();
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<Watch> findOneById(@PathVariable Long id){
        Optional<Watch> optWatch = watchService.findOneById(id);
        if (optWatch.isEmpty())
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(optWatch.get(), HttpStatus.OK);
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Watch createWatch(@RequestBody Watch watch){
        return watchService.createWatch(watch);
    }

    @PatchMapping(consumes = "application/json")
    public Watch updateWatch(@RequestBody Watch watch){
        return  watchService.updateWatch(watch);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id){
        try{
            watchService.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
        }
    }
}
