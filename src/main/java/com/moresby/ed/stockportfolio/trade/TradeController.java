package com.moresby.ed.stockportfolio.trade;

import com.moresby.ed.stockportfolio.exception.InsufficientAmount;
import com.moresby.ed.stockportfolio.trade.model.entity.Trade;
import com.moresby.ed.stockportfolio.trade.model.pojo.TradePOJO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/trades")
@AllArgsConstructor
public class TradeController {

    private final TradeService tradeService;

    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<Trade> findByTradeId(@PathVariable("id") Long tradeId) throws InterruptedException {
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

    @PostMapping
    public Optional<Trade> executeTrade(@RequestBody TradePOJO tradePOJO) throws InterruptedException {
        Thread.sleep(3000); // TODO: remove this line when production
        Optional<Trade> optTrade = Optional.empty();
        try{
            Trade trade = tradeService.executeTrade(tradePOJO);
            optTrade = Optional.of(trade);
        } catch (InsufficientAmount e){
            System.out.println("Transaction failed");
            System.out.println(e.getMessage());
        }
        return optTrade;
    }

}
