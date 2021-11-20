package com.moresby.ed.stockportfolio.tstock;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/stock")
@AllArgsConstructor
public class TStockController {

    private final TStockService tStockService;

    @GetMapping(path = "/findAll", produces = "application/json")
    public Iterable<TStock> findAllStocks(){
        return tStockService.findAllStocks();
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<TStock> findStockById(@PathVariable Long id){
        Optional<TStock> optStock = tStockService.findStock(id);
        if(optStock.isEmpty())
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(optStock.get(), HttpStatus.OK);
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public TStock createStock(@RequestBody TStock TStock){
        return tStockService.createStock(TStock);
    }

    @PatchMapping
    public TStock updateStock(@RequestBody TStock TStock){
        return tStockService.updateStock(TStock);
    }

    @DeleteMapping(path = "{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStock(@PathVariable Long id){
        tStockService.deleteStock(id);
    }


    @GetMapping(path = "/refresh")
    @Transactional
    public void refreshStockOfPrice(){
        tStockService.refreshPriceOfStocks();
    }
}
