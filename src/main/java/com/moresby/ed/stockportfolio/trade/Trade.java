package com.moresby.ed.stockportfolio.trade;

import com.moresby.ed.stockportfolio.tstock.TStock;
import com.moresby.ed.stockportfolio.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;

@Entity(name = "Trade")
@Table(name = "trade")
@NoArgsConstructor
@Getter
@Setter
public class Trade {

    @EmbeddedId
    private TradeId tradeId;

    @ManyToOne
    @MapsId(value = "userId")
    @JoinColumn(
            name = "userId",
            foreignKey = @ForeignKey(name = "trade_user_fk")
    )
    private User user;

    @ManyToOne
    @MapsId(value = "tStockId")
    @JoinColumn(
            name = "tStock_id",
            foreignKey = @ForeignKey(name = "trade_tStock_fk")
    )
    private TStock tStock;

    @Column(name = "volume")
    private Integer volume;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "cost")
    private BigDecimal cost;

    @Column(name = "trade_date")
    private Date tradeDate;

    @Column(name = "trade_time")
    private Time tradeTime;
}
