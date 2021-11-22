package com.moresby.ed.stockportfolio.classify;

import com.moresby.ed.stockportfolio.tstock.TStock;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yahoofinance.Stock;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/classifies")
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class ClassifyController {

    private final ClassifyService classifyService;

    @GetMapping(path = "/findAll", produces = "application/json")
    public Iterable<Classify> findAllClassify() throws InterruptedException {
        Thread.sleep(3000); // TODO: remove this line when production

        return classifyService.findAllClassify();
    }

    @GetMapping(path = "/{classifyId}")
    public List<TStock> findStocksByClassifyId(@PathVariable("classifyId") Integer classifyId){
        return classifyService.findStocksByClassifyId(classifyId);
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
