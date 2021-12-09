package com.moresby.ed.stockportfolio.trade;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface TradeService {

    Optional<Trade> findByTradeId(TradeId tradeId);
    List<Trade> findOneByUserIdAndTradeDate(Long userId, Date tradeDate);
    Iterable<Trade> findAll();
    Trade newTrade(Trade trade);
}
