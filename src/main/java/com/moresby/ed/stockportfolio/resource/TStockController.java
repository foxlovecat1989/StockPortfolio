package com.moresby.ed.stockportfolio.resource;

import com.moresby.ed.stockportfolio.domain.TStock;
import com.moresby.ed.stockportfolio.exception.domain.stock.ConnectErrorException;
import com.moresby.ed.stockportfolio.exception.domain.stock.StockExistException;
import com.moresby.ed.stockportfolio.exception.domain.stock.StockNotfoundException;
import com.moresby.ed.stockportfolio.exception.handler.StockExceptionHandling;
import com.moresby.ed.stockportfolio.service.TStockService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import yahoofinance.histquotes.HistoricalQuote;

import javax.transaction.Transactional;
import javax.websocket.server.PathParam;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/api/v1/stock")
@AllArgsConstructor
public class TStockController extends StockExceptionHandling {

    private final TStockService tStockService;

    @GetMapping(path = "/findAll", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TStock>> findAllStocks() {
        List<TStock> tStocks = tStockService.findAllStocks();

        return new ResponseEntity<>(tStocks, HttpStatus.OK);
    }

    @GetMapping(path = "/{symbol}", produces = APPLICATION_JSON_VALUE)
    @PreAuthorize(value = "hasAnyAuthority('stock:read')")
    public ResponseEntity<TStock> findStockBySymbol(@PathVariable("symbol") String symbol)
            throws StockNotfoundException {
        TStock stock = tStockService.findExistingStockBySymbol(symbol);

        return new ResponseEntity<>(stock, HttpStatus.OK);
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    @PreAuthorize(value = "hasAnyAuthority('stock:read')")
    public ResponseEntity<TStock> findStockByName(@PathParam("stockName") String stockName)
            throws StockNotfoundException {
        TStock stock = tStockService.findExistingStockByName(stockName);

        return new ResponseEntity<>(stock, HttpStatus.OK);
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    @PreAuthorize(value = "hasAnyAuthority('manage:create', 'admin:create')")
    public ResponseEntity<TStock> createStock(@RequestBody TStock tStock)
            throws StockExistException {
        var newStock = tStockService.createStock(tStock);

        return new ResponseEntity<>(newStock, HttpStatus.CREATED);
    }

    @PatchMapping(consumes = APPLICATION_JSON_VALUE)
    @PreAuthorize(value = "hasAnyAuthority('manage:update', 'admin:update')")
    public TStock updateStock(@RequestBody TStock TStock)
            throws StockNotfoundException, StockExistException {

        return tStockService.updateStock(TStock);
    }

    @GetMapping(path = "/refresh")
    @Transactional
    @PreAuthorize(value = "hasAnyAuthority('stock:read')")
    public void refreshStockOfPrice(){
        tStockService.refreshPriceOfStocks();
    }

    @GetMapping(value = {"/histquotes/{symbol:.+}/{month}"}, produces = APPLICATION_JSON_VALUE)
    @PreAuthorize(value = "hasAnyAuthority('stock:read')")
    public ResponseEntity<List<HistoricalQuote>> queryHistoricalQuotes(
            @PathVariable("symbol") String symbol,
            @PathVariable("month") Integer month)
            throws ConnectErrorException {
       var historicalQuotes = tStockService.getHistoricalQuotes(symbol, month);

        return new ResponseEntity<>(historicalQuotes, HttpStatus.OK);
    }
}
