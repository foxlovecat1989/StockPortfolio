package com.moresby.ed.stockportfolio.service.impl;

import com.moresby.ed.stockportfolio.exception.domain.trade.*;
import com.moresby.ed.stockportfolio.exception.domain.user.UserNotFoundException;
import com.moresby.ed.stockportfolio.repository.TradeRepository;
import com.moresby.ed.stockportfolio.service.AccountService;
import com.moresby.ed.stockportfolio.service.InventoryService;
import com.moresby.ed.stockportfolio.domain.Trade;
import com.moresby.ed.stockportfolio.domain.TradePOJO;
import com.moresby.ed.stockportfolio.service.TradeService;
import com.moresby.ed.stockportfolio.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

import static com.moresby.ed.stockportfolio.constant.TradeImplConstant.NO_TRADE_FOUND_BY_TRADE_ID;
import static com.moresby.ed.stockportfolio.constant.CommonConstant.TIME_DIFF_PLUS_EIGHT_HOURS_IN_MILLISECONDS;

@Service
@RequiredArgsConstructor
@Slf4j
public class TradeServiceImpl implements TradeService {

    private final TradeRepository tradeRepository;
    private final InventoryService inventoryService;
    private final AccountService accountService;
    private final UserService userService;

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
    public List<Trade> findAllByUserNumberAndTradeDate(String userNumber, Date tradeDate) throws UserNotFoundException {
        var user = userService.findExistingUserByUserNumber(userNumber);

        return tradeRepository.findAllByUserNumberAndTradeDateEquals(user.getUserNumber(), tradeDate);
    }

    @Override
    public List<Trade> findAll() {
        return tradeRepository.findAll();
    }

    @Override
    @Transactional(rollbackFor = {Throwable.class})
    public Trade executeTrade(TradePOJO tradePOJO)
            throws
            InSufficientBalanceException,
            BankAccountNotFoundException,
            InSufficientAmountInInventoryException,
            InputNumberNegativeException, InventoryNotFoundException, UserNotFoundException {

        inventoryService.updateInventory(tradePOJO);

        var tradeAmount =
                Math.round(tradePOJO.getTStock().getPrice().doubleValue() * tradePOJO.getAmount());

       accountService.executeTrade(tradePOJO, tradeAmount);


        return createTrade(tradePOJO);
    }

    private Trade createTrade(TradePOJO tradePOJO) {
        Trade trade = new Trade();
        trade.setUser(tradePOJO.getUser());
        trade.setTStock(tradePOJO.getTStock());
        trade.setPrice(tradePOJO.getTStock().getPrice());
        trade.setAmount(BigDecimal.valueOf(tradePOJO.getAmount()));
        trade.setTradeType(tradePOJO.getTradeType());
        var currentDateTime = new java.util.Date().getTime() + TIME_DIFF_PLUS_EIGHT_HOURS_IN_MILLISECONDS;
        trade.setTradeDate(new java.sql.Date(currentDateTime));
        trade.setTradeTime(new Time(currentDateTime));

        return tradeRepository.save(trade);
    }
}
