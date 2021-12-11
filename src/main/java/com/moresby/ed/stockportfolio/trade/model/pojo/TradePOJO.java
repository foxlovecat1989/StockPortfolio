package com.moresby.ed.stockportfolio.trade.model.pojo;

import com.moresby.ed.stockportfolio.trade.model.enumeration.TradeType;
import com.moresby.ed.stockportfolio.tstock.TStock;
import com.moresby.ed.stockportfolio.user.User;
import lombok.*;

@Getter
@Setter
public class TradePOJO {
    private  TStock tStock;
    private  User user;
    private  Long amount;
    private TradeType tradeType;

    public TradePOJO() {
    }

    @Builder
    public TradePOJO(TStock tStock, User user, Long amount) {
        this.tStock = tStock;
        this.user = user;
        this.amount = amount;
    }
}
