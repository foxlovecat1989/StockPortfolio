package com.moresby.ed.stockportfolio.service.impl;

import com.github.javafaker.Faker;
import com.moresby.ed.stockportfolio.exception.domain.trade.*;
import com.moresby.ed.stockportfolio.repository.TradeRepository;
import com.moresby.ed.stockportfolio.service.AccountService;
import com.moresby.ed.stockportfolio.service.InventoryService;
import com.moresby.ed.stockportfolio.domain.Trade;
import com.moresby.ed.stockportfolio.enumeration.TradeType;
import com.moresby.ed.stockportfolio.domain.TradePOJO;
import com.moresby.ed.stockportfolio.service.TradeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

import static com.moresby.ed.stockportfolio.constant.TradeConstant.NO_TRADE_FOUND_BY_TRADE_ID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TradeServiceImpl implements TradeService {

    private final TradeRepository tradeRepository;
    private final InventoryService inventoryService;
    private final AccountService accountService;
    private final Faker faker;
    private static final int MILLISECONDS_IN_ONE_DAY = 86400 * 1000;

    @Override
    public Trade findExistingTradeByTradeId(Long tradeId) throws TradeNotFoundException {
        return tradeRepository.findById(tradeId)
                .orElseThrow(
                        () -> {
                            var errorMsg = String.format(NO_TRADE_FOUND_BY_TRADE_ID, tradeId);
                            log.error(errorMsg);
                            return new TradeNotFoundException(errorMsg);
                        }
                );
    }

    @Override
    public List<Trade> findOneByUserIdAndTradeDate(Long userId, Date tradeDate) {
        return tradeRepository.findAllByUserIdAndTradeDateEquals(userId, tradeDate);
    }

    @Override
    public List<Trade> findAll() {
        return tradeRepository.findAll();
    }

    @Override
    @Transactional
    public Trade executeTrade(TradePOJO tradePOJO)
            throws
            InSufficientBalanceException,
            BankAccountNotFoundException,
            InSufficientAmountInInventoryException,
            InputNumberNegativeException {

        inventoryService.updateInventory(tradePOJO);

        var tradeAmount =
                Math.round(tradePOJO.getTStock().getPrice().doubleValue() * tradePOJO.getAmount());
        if (tradePOJO.getTradeType() == TradeType.BUY)
            accountService.withdrawal(tradePOJO.getUser(), tradeAmount);
        else
            accountService.deposit(tradePOJO.getUser(), tradeAmount);


        return createTrade(tradePOJO);
    }

    private Trade createTrade(TradePOJO tradePOJO) {
        Trade trade = new Trade();
        trade.setUser(tradePOJO.getUser());
        trade.setTStock(tradePOJO.getTStock());
        trade.setPrice(tradePOJO.getTStock().getPrice());
        trade.setAmount(BigDecimal.valueOf(tradePOJO.getAmount()));
        trade.setTradeType(tradePOJO.getTradeType());
//        trade.setTradeDate(
//                new java.sql.Date(
//                        new java.util.Date().getTime())
//        );
//        trade.setTradeTime(new Time(new java.util.Date().getTime()));
        // TODO: CHANGE THESE FAKER DATE AND TIME WHEN PRODUCTION
        int fakeTime = MILLISECONDS_IN_ONE_DAY * faker.number().numberBetween(1, 10);
        Date tradeDate =
                new Date(
                        new java.util.Date().getTime()
                                + (tradePOJO.getTradeType() == TradeType.BUY ? fakeTime : -fakeTime));

        trade.setTradeDate(tradeDate);
        trade.setTradeTime(Time.valueOf(String.format("%d:00:00", faker.number().numberBetween(9, 12))));
        tradeRepository.save(trade);


        return trade;
    }
}
