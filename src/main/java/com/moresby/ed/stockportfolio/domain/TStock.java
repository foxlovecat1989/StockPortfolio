package com.moresby.ed.stockportfolio.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "TStock")
@Table(name = "tstock")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(value = "trades")
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
            name = "previous_closed",
            columnDefinition="Decimal(10,2) default '0.00'"
    )
    private BigDecimal previousClosed;

    @Column(
            name = "price",
            columnDefinition="Decimal(10,2) default '0.00'"
    )
    private BigDecimal price;

    @Column(
            name = "change_price",
            columnDefinition="Decimal(10,2) default '0.00'"
    )
    private BigDecimal changePrice;

    @Column(
            name = "change_in_percent",
            columnDefinition="Decimal(10,2) default '0.00'"
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

    @OneToMany(
            mappedBy = "tStock"
    )
    private List<Trade> trades;

    public TStock(String symbol, String name, Classify classify) {
        this.symbol = symbol;
        this.name = name;
        this.classify = classify;
    }
}
