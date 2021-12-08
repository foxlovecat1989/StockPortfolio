package com.moresby.ed.stockportfolio.activity;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/activities")
public class ActivityController {

    private final ActivityService activityService;

    @GetMapping(path = "findAll", produces = "application/json")
    public Iterable<Activity> findAll() throws InterruptedException {
        Thread.sleep(3000); // TODO: remove when production

        return activityService.findAll();
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<Activity> findById(@PathVariable Long id) throws InterruptedException {
        Thread.sleep(3000); // TODO: remove when production
        Optional<Activity> optActivity = activityService.findById(id);
        if (optActivity.isEmpty())
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(optActivity.get(), HttpStatus.OK);
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Activity createOne(@RequestBody Activity activity) throws InterruptedException {
        Thread.sleep(3000); // TODO: remove when production

        return activityService.create(activity);
    }

    @PatchMapping(consumes = "application/json")
    public Activity update(@RequestBody Activity activity) throws InterruptedException {
        Thread.sleep(3000); // TODO: remove when production

        return activityService.update(activity);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) throws InterruptedException {
        Thread.sleep(3000); // TODO: remove when production
        try{
            activityService.deleteById(id);
        }catch (EmptyResultDataAccessException e){
            e.printStackTrace();
        }
    }
}
