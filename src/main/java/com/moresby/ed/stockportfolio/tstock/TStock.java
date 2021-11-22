package com.moresby.ed.stockportfolio.tstock;

import com.moresby.ed.stockportfolio.classify.Classify;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity(name = "TStock")
@Table(name = "tstock")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TStock {
    @SequenceGenerator(
            name = "tstock_sequence",
            sequenceName = "tstock_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "tstock_sequence"
    )
    @Column(name = "id")
    @Id
    private Long id;

    @Column(
            name = "symbol",
            nullable = false,
            updatable = false,
            unique = true,
            columnDefinition = "TEXT"
    )
    private String symbol;

    @Column(
            name = "name",
            nullable = false,
            updatable = false,
            unique = true,
            columnDefinition = "TEXT"
    )
    private String name;

    @Column(
            name = "previous_closed"
    )
    private BigDecimal previousClosed;

    @Column(
            name = "price"
    )
    private BigDecimal price;

    @Column(
            name = "change_price"
    )
    private BigDecimal changePrice;

    @Column(
            name = "change_in_percent"
    )
    private BigDecimal changeInPercent;


    @Column(
            name = "volume"
    )
    private Long volume;

    @Column(
            name = "lastUpDateTime",
            columnDefinition = "TIMESTAMP WITHOUT TIME ZONE"
    )
    private LocalDateTime lastUpDateTime;

    @ManyToOne
    @JoinColumn(
            name = "classify_id",
            referencedColumnName = "classify_id",
            foreignKey = @ForeignKey(name = "stock_classify_fk")
    )
    private Classify classify;

    public TStock(String symbol, String name, Classify classify) {
        this.symbol = symbol;
        this.name = name;
        this.classify = classify;
    }
}
