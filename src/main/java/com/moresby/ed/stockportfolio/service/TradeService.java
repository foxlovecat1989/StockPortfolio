package com.moresby.ed.stockportfolio.service;

import com.moresby.ed.stockportfolio.domain.Trade;
import com.moresby.ed.stockportfolio.domain.TradePOJO;
import com.moresby.ed.stockportfolio.exception.domain.trade.*;
import com.moresby.ed.stockportfolio.exception.domain.user.UserNotFoundException;

import java.sql.Date;
import java.util.List;

public interface TradeService {

    Trade findExistingTradeByTradeId(Long tradeId) throws TradeNotFoundException;
    List<Trade> findOneByUserIdAndTradeDate(Long userId, Date tradeDate);
    List<Trade> findRecentTrade(String userNumber) throws UserNotFoundException;
    List<Trade> findAll();
    Trade executeTrade(TradePOJO tradePOJO)
            throws
            InSufficientBalanceException,
            BankAccountNotFoundException,
            InSufficientAmountInInventoryException,
            InputNumberNegativeException, InventoryNotFoundException;
}
