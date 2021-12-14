package com.moresby.ed.stockportfolio.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.moresby.ed.stockportfolio.enumeration.TradeType;
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

    @Column(
            name = "amount",
            columnDefinition="Decimal(10,2) default '0.00'"
    )
    private BigDecimal amount;

    @Column(
            name = "price",
            columnDefinition="Decimal(10,2) default '0.00'"
    )
    private BigDecimal price;

    @Column(
            name = "trade_date",
            columnDefinition = "DATE"
    )
    private Date tradeDate;

    @Column(
            name = "trade_time",
            columnDefinition = "TIME"
    )
    private Time tradeTime;

    @Column(name = "trade_type")
    @Enumerated(value = EnumType.STRING)
    private TradeType tradeType;

}
