package com.moresby.ed.stockportfolio.trade;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/trades")
@RequiredArgsConstructor
public class TradeController {
    private final TradeService tradeService;

    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<Trade> findByTradeId(@PathVariable("id") TradeId tradeId) throws InterruptedException {
        Thread.sleep(3000); // TODO: remove this line when production
        Optional<Trade> optTrade = tradeService.findByTradeId(tradeId);
        if (optTrade.isEmpty())
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(optTrade.get(), HttpStatus.OK);
    }


    @GetMapping(path = "/findAll", produces = "application/json")
    public Iterable<Trade> findAll() throws InterruptedException {
        Thread.sleep(3000); // TODO: remove this line when production

        return tradeService.findAll();
    }

    @GetMapping(path = "/findAll/{userId}/{tradeDate}", produces = "application/json")
    public Iterable<Trade> findAllByUserIdAndTradeDate(
            @PathVariable("userId") Long userId,
            @PathVariable("tradeDate") Date tradeDate
            ) throws InterruptedException {
        Thread.sleep(3000); // TODO: remove this line when production

        return tradeService.findOneByUserIdAndTradeDate(userId, tradeDate);
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Trade createOne(@RequestBody Trade trade) throws InterruptedException {
        Thread.sleep(3000); // TODO: remove this line when production

        return tradeService.newTrade(trade);
    }

}
