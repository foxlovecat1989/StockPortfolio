package com.moresby.ed.stockportfolio.classify;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/classify")
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class ClassifyController {

    private final ClassifyService classifyService;

    @GetMapping(path = "/findAll", produces = "application/json")
    public Iterable<Classify> findAllClassify(){
        return classifyService.findAllClassify();
    }

    @GetMapping(path = "{id}", produces = "application/json")
    public ResponseEntity<Classify> findClassifyById(@PathVariable(value = "id") Integer classifyId){
        Optional<Classify> optClassify =  classifyService.findClassifyById(classifyId);
        if (optClassify.isEmpty())
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(optClassify.get(), HttpStatus.OK);
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Classify createClassify(@RequestBody Classify classify){
        return  classifyService.createClassify(classify);
    }

    @PatchMapping
    public Classify updateClassify(@RequestBody Classify classify){
        return  classifyService.updateClassify(classify);
    }

    @DeleteMapping(path = "{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteClassify(@PathVariable(value = "id") Integer classifyId){
        classifyService.deleteById(classifyId);
    }
}
