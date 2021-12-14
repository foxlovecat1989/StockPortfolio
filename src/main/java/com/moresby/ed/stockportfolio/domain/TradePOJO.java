package com.moresby.ed.stockportfolio.domain;

import com.moresby.ed.stockportfolio.enumeration.TradeType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class TradePOJO {
    private  TStock tStock;
    private  User user;
    private  Long amount;
    private TradeType tradeType;

    @Builder
    public TradePOJO(TStock tStock, User user, Long amount, TradeType tradeType) {
        this.tStock = tStock;
        this.user = user;
        this.amount = amount;
        this.tradeType = tradeType;
    }
}
