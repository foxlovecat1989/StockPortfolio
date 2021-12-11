package com.moresby.ed.stockportfolio.trade;

import com.moresby.ed.stockportfolio.trade.model.entity.Trade;
import com.moresby.ed.stockportfolio.trade.model.pojo.TradePOJO;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface TradeService {

    Optional<Trade> findByTradeId(Long tradeId);
    List<Trade> findOneByUserIdAndTradeDate(Long userId, Date tradeDate);
    Iterable<Trade> findAll();
    Trade buy(TradePOJO tradePOJO);
    Trade sell(TradePOJO tradePOJO);
}
