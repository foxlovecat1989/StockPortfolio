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
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize(value = "hasAnyAuthority('trade:read')")
    public ResponseEntity<Trade> findByTradeId(@PathVariable("tradeId") Long tradeId)
            throws TradeNotFoundException {
        Trade trade = tradeService.findExistingTradeByTradeId(tradeId);

        return new ResponseEntity<>(trade, HttpStatus.OK);
    }


    @GetMapping(path = "/findAll", produces = APPLICATION_JSON_VALUE)
    @PreAuthorize(value = "hasAnyAuthority('manage:read', 'admin:read')")
    public ResponseEntity<List<Trade>> findAll() {
        var trades = tradeService.findAll();

        return new ResponseEntity<>(trades, HttpStatus.OK);
    }

    @GetMapping(path = "/findAll/{userNumber}/{tradeDate}", produces = APPLICATION_JSON_VALUE)
    @PreAuthorize(value = "hasAnyAuthority('trade:read')")
    public ResponseEntity<List<Trade>> findAllByUserNumberAndTradeDate (
            @PathVariable("userNumber") String userNumber,
            @PathVariable("tradeDate") Date tradeDate
            ) throws UserNotFoundException {
        var trades = tradeService.findAllByUserNumberAndTradeDate(userNumber, tradeDate);

        return new ResponseEntity<>(trades, HttpStatus.OK);
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    @PreAuthorize(value = "hasAnyAuthority('trade:create')")
    public ResponseEntity<Trade> executeTrade(@RequestBody TradePOJO tradePOJO)
            throws
            BankAccountNotFoundException,
            InSufficientBalanceException,
            InSufficientAmountInInventoryException,
            InputNumberNegativeException,
            InventoryNotFoundException, UserNotFoundException {
        var trade = tradeService.executeTrade(tradePOJO);

        return new ResponseEntity<>(trade, HttpStatus.CREATED);
    }

}
