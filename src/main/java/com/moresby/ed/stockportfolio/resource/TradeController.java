package com.moresby.ed.stockportfolio.resource;

import com.moresby.ed.stockportfolio.domain.Trade;
import com.moresby.ed.stockportfolio.domain.TradePOJO;
import com.moresby.ed.stockportfolio.exception.domain.trade.*;
import com.moresby.ed.stockportfolio.exception.domain.user.UserNotFoundException;
import com.moresby.ed.stockportfolio.exception.handler.TradeExceptionHandling;
import com.moresby.ed.stockportfolio.service.TradeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/api/v1/trade")
@AllArgsConstructor
@Slf4j
public class TradeController extends TradeExceptionHandling {

    private final TradeService tradeService;

    @GetMapping(path = "/{tradeId}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Trade> findByTradeId(@PathVariable("tradeId") Long tradeId)
            throws InterruptedException, TradeNotFoundException {
        Thread.sleep(3000); // TODO: remove this line when production
        Trade trade = tradeService.findExistingTradeByTradeId(tradeId);

        return new ResponseEntity<>(trade, HttpStatus.OK);
    }


    @GetMapping(path = "/findAll", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Trade>> findAll() throws InterruptedException {
        Thread.sleep(3000); // TODO: remove this line when production
        var trades = tradeService.findAll();

        return new ResponseEntity<>(trades, HttpStatus.OK);
    }

    @GetMapping(path = "/findAll/{userId}/{tradeDate}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Trade>> findAllByUserIdAndTradeDate(
            @PathVariable("userId") Long userId,
            @PathVariable("tradeDate") Date tradeDate
            ) throws InterruptedException {
        Thread.sleep(3000); // TODO: remove this line when production
        var trades = tradeService.findOneByUserIdAndTradeDate(userId, tradeDate);

        return new ResponseEntity<>(trades, HttpStatus.OK);
    }

    @GetMapping(path = "/recentTrade/findAll/{userNumber}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Trade>> findAllByUserIdAndTradeDate(@PathVariable("userNumber") String userNumber)
            throws InterruptedException, UserNotFoundException {
        Thread.sleep(3000); // TODO: remove this line when production
        var trades = tradeService.findRecentTrade(userNumber);

        return new ResponseEntity<>(trades, HttpStatus.OK);
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Trade> executeTrade(@RequestBody TradePOJO tradePOJO)
            throws
            InterruptedException,
            BankAccountNotFoundException,
            InSufficientBalanceException,
            InSufficientAmountInInventoryException,
            InputNumberNegativeException {
        Thread.sleep(3000); // TODO: remove this line when production
        var trade = tradeService.executeTrade(tradePOJO);

        return new ResponseEntity<>(trade, HttpStatus.CREATED);
    }

}
