package com.moresby.ed.stockportfolio.resource;

import com.moresby.ed.stockportfolio.service.ClassifyService;
import com.moresby.ed.stockportfolio.domain.Classify;
import com.moresby.ed.stockportfolio.domain.TStock;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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
    public List<TStock> findStocksByClassifyId(@PathVariable("classifyId") Integer classifyId) throws InterruptedException {
        Thread.sleep(3000); // TODO: remove this line when production

        return classifyService.findStocksByClassifyId(classifyId);
    }


    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Classify createClassify(@RequestBody Classify classify) throws InterruptedException {
        Thread.sleep(3000); // TODO: remove this line when production

         return  classifyService.createClassify(classify);
    }

    @PatchMapping
    public Classify updateClassify(@RequestBody Classify classify) throws InterruptedException {
        Thread.sleep(3000); // TODO: remove this line when production

        return  classifyService.updateClassify(classify);
    }

}
