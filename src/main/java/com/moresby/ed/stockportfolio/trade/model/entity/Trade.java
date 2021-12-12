package com.moresby.ed.stockportfolio.trade.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.moresby.ed.stockportfolio.trade.model.enumeration.TradeType;
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
@Setter
@Getter
@JsonIgnoreProperties(value = {"user"})
public class Trade {

    @Id
    @SequenceGenerator(
            name = "trade_sequence",
            sequenceName = "trade_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "trade_sequence"
    )
    private Long tradeId;

    @ManyToOne
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "trade_user_fk")
    )
    private User user;

    @ManyToOne
    @JoinColumn(
            name = "tstock_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "trade_tstock_fk")
    )
    private TStock tStock;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "trade_date")
    private Date tradeDate;

    @Column(name = "trade_time")
    private Time tradeTime;

    @Column(name = "trade_type")
    @Enumerated(value = EnumType.STRING)
    private TradeType tradeType;

}
