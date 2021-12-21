package com.moresby.ed.stockportfolio.resource;

import com.moresby.ed.stockportfolio.domain.TStock;
import com.moresby.ed.stockportfolio.exception.domain.stock.StockExistException;
import com.moresby.ed.stockportfolio.exception.domain.stock.StockNotfoundException;
import com.moresby.ed.stockportfolio.exception.handler.StockExceptionHandling;
import com.moresby.ed.stockportfolio.service.TStockService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.transaction.Transactional;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/api/v1/stocks")
@AllArgsConstructor
public class TStockController extends StockExceptionHandling {

    private final TStockService tStockService;

    @GetMapping(path = "/findAll", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TStock>> findAllStocks() throws InterruptedException {
        Thread.sleep(3000); // TODO: remove this line when production
        List<TStock> tStocks = tStockService.findAllStocks();

        return new ResponseEntity<>(tStocks, HttpStatus.OK);
    }

    @GetMapping(path = "/{symbol}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<TStock> findStockBySymbol(@PathVariable String symbol)
            throws InterruptedException, StockNotfoundException {
        Thread.sleep(3000); // TODO: remove this line when production
        TStock stock = tStockService.findExistingStockBySymbol(symbol);

        return new ResponseEntity<>(stock, HttpStatus.OK);
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<TStock> createStock(@RequestBody TStock tStock)
            throws InterruptedException, StockExistException {
        Thread.sleep(3000); // TODO: remove this line when production
        var newStock = tStockService.createStock(tStock);

        return new ResponseEntity<>(newStock, HttpStatus.CREATED);
    }

    @PatchMapping(consumes = APPLICATION_JSON_VALUE)
    public TStock updateStock(@RequestBody TStock TStock)
            throws InterruptedException, StockNotfoundException {
        Thread.sleep(3000); // TODO: remove this line when production

        return tStockService.updateStock(TStock);
    }

    @GetMapping(path = "/refresh")
    @Transactional
    public void refreshStockOfPrice(){
        tStockService.refreshPriceOfStocks();
    }
}
