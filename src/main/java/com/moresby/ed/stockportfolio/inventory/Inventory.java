package com.moresby.ed.stockportfolio.inventory;

import com.moresby.ed.stockportfolio.tstock.TStock;
import com.moresby.ed.stockportfolio.user.User;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.math.BigDecimal;

@Entity(name = "Inventory")
@Table(name = "inventory")
@Getter
@Setter
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

    @Column(name = "avg_price")
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
            foreignKey = @ForeignKey(name = "inventory_tStock_fk")
    )
    private TStock tStock;
}
