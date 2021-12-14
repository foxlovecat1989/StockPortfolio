package com.moresby.ed.stockportfolio.service;

import com.moresby.ed.stockportfolio.domain.Trade;
import com.moresby.ed.stockportfolio.domain.TradePOJO;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface TradeService {

    Optional<Trade> findByTradeId(Long tradeId);
    List<Trade> findOneByUserIdAndTradeDate(Long userId, Date tradeDate);
    Iterable<Trade> findAll();
    Trade executeTrade(TradePOJO tradePOJO);
}
