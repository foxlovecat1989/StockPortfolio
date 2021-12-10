package com.moresby.ed.stockportfolio.inventory;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity(name = "Inventory")
@Subselect(
        "SELECT " +
                "ROW_NUMBER() OVER() AS id, " +
                "au.id AS user_id, " +
                "s.id AS stock_id, " +
                "SUM(t.amount) AS amount, " +
                "SUM(t.cost) AS total_cost, " +
                "AVG(t.cost / t.amount) AS avg_price, " +
                "t.price AS current_price " +
        "FROM app_user au, trade t, tstock s " +
        "WHERE au.id = t.user_id AND t.t_stock_id = s.id " +
        "GROUP BY au.id, s.id, t.price"
)
@Synchronize(value = {"app_user", "trade", "tstock"})
@Immutable
@Getter
@Setter
public class Inventory {

    @Id
    private Long id;

    @Column(name = "avg_price")
    private Double avgPrice;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "stock_id")
    private Long stockId;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "total_cost")
    private BigDecimal totalCost;

    @Column(name = "current_price")
    private BigDecimal currentPrice;

}
