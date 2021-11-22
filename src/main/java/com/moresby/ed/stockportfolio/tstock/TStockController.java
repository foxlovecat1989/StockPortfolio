package com.moresby.ed.stockportfolio.tstock;

import com.moresby.ed.stockportfolio.model.Response;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/stocks")
@AllArgsConstructor
public class TStockController {

    private final TStockService tStockService;

    @GetMapping(path = "/findAll", produces = "application/json")
    public Iterable<TStock> findAllStocks() throws InterruptedException {
        Thread.sleep(3000); // TODO: remove this line when production

        return tStockService.findAllStocks();
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<TStock> findStockById(@PathVariable Long id) throws InterruptedException {
        Thread.sleep(3000); // TODO: remove this line when production

        Optional<TStock> optStock = tStockService.findStock(id);
        if(optStock.isEmpty())
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(optStock.get(), HttpStatus.OK);
    }

    @PostMapping(consumes = "application/json")
    public TStock createStock(@RequestBody TStock TStock) throws InterruptedException {
        Thread.sleep(3000); // TODO: remove this line when production

        return tStockService.createStock(TStock);
    }

    @PatchMapping(consumes = "application/json")
    public TStock updateStock(@RequestBody TStock TStock) throws InterruptedException {
        Thread.sleep(3000); // TODO: remove this line when production

        return tStockService.updateStock(TStock);
    }

    @DeleteMapping(path = "{id}")
    public void deleteStock(@PathVariable Long id) throws InterruptedException {
        Thread.sleep(3000); // TODO: remove this line when production

        tStockService.deleteStock(id);
    }


    @GetMapping(path = "/refresh")
    @Transactional
    public void refreshStockOfPrice(){
        tStockService.refreshPriceOfStocks();
    }
}
