package com.moresby.ed.stockportfolio.inventoryreport;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity(name = "InventReport")
@Immutable
@Subselect(
    "SELECT ROW_NUMBER() OVER() AS id, " +
            "       au.id AS user_id, " +
            "       concat(s.name, '-', SUBSTR(s.symbol, 1, 4)) AS stock_name, " +
            "       c.name AS classify_name, " +
            "       i.amount AS amount_in_inventory, " +
            "       (s.price + 10) AS current_price, " +
            "       i.amount * (s.price + 10) AS current_total_worth, " +
            "       SUM(i.avg_price * i.amount) AS total_cost, " +
            "       i.avg_price AS avg_price_in_inventory, " +
            "       i.amount * (s.price + 10) - sum(i.avg_price * i.amount) AS income, " +
            "       ROUND( " +
            "           CAST( " +
            "                ((i.amount * (s.price + 10) - sum(i.avg_price * i.amount)) / sum((i.avg_price * i.amount)) * 100) AS DECIMAL " +
            "               ), 2) AS rate_of_return " +
            "FROM app_user au, inventory i, tstock s, classify c " +
            "WHERE au.id = i.user_id AND i.tstock_id = s.id AND s.classify_id = c.classify_id " +
            "GROUP BY au.id, s.name, s.symbol, c.name, i.amount, i.avg_price, s.price"
)
// TODO: MODIFY S.PRICE + 10 TO S.PRICE
@Getter
@Setter
public class InventoryReport {
    @Id
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "stock_name")
    private String stockName;

    @Column(name = "classify_name")
    private String classifyName;

    @Column(name = "amount_in_inventory")
    private BigDecimal amountInInventory;

    @Column(name = "current_price")
    private BigDecimal currentPrice;

    @Column(name = "current_total_worth")
    private BigDecimal currentTotalWorth;

    @Column(name = "total_cost")
    private BigDecimal totalCost;

    @Column(name = "avg_price_in_inventory")
    private BigDecimal avgPriceInInventory;

    @Column(name = "income")
    private BigDecimal income;

    @Column(name = "rate_of_return")
    private double rateOfReturn;
}
