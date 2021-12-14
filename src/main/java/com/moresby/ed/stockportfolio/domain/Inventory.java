package com.moresby.ed.stockportfolio.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.math.BigDecimal;

@Entity(name = "Inventory")
@Table(name = "inventory")
@Getter
@Setter
@JsonIgnoreProperties(value = {"user"})
public class Inventory {

    @Id
    @SequenceGenerator(
            name = "inventory_sequence",
            sequenceName = "inventory_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "inventory_sequence"
    )
    private Long id;

    @Column(name = "amount")
    private Long amount;

    @Column(
            name = "avg_price",
            columnDefinition="Decimal(10,2) default '0.00'"
    )
    private BigDecimal avgPrice;

    @ManyToOne
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "inventory_user_fk")
    )
    private User user;

    @ManyToOne
    @JoinColumn(
            name = "tstock_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "inventory_tstock_fk")
    )
    private TStock tStock;
}
