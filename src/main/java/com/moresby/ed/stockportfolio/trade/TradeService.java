package com.moresby.ed.stockportfolio.trade;

import com.moresby.ed.stockportfolio.tstock.TStock;
import com.moresby.ed.stockportfolio.user.User;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface TradeService {

    Optional<Trade> findByTradeId(Long tradeId);
    List<Trade> findOneByUserIdAndTradeDate(Long userId, Date tradeDate);
    Iterable<Trade> findAll();
    Trade buy(Long userId, Long stockId, Integer amount);
    Trade sell(Long userId, Long stockId, Integer amount);
}
