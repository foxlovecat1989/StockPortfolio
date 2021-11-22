package com.moresby.ed.stockportfolio.Watchlist;

import com.moresby.ed.stockportfolio.tstock.TStock;
import com.moresby.ed.stockportfolio.watch.Watch;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "Watchlist")
@Table(name = "watchlist")
@NoArgsConstructor
@Getter
@Setter
public class Watchlist {

    @EmbeddedId
    private WatchlistId watchlistId;

    @ManyToOne
    @MapsId(value = "watchId")
    @JoinColumn(
            name = "watch_id",
            foreignKey = @ForeignKey(name = "watchlist_fk")
    )
    private Watch watch;

    @ManyToOne
    @MapsId(value = "tStockId")
    @JoinColumn(
            name = "tStock_id",
            foreignKey = @ForeignKey(name = "watchlist_tStock_fk")
    )
    private TStock tStock;

    @Column(
            name = "last_update_date",
            columnDefinition = "TIMESTAMP WITHOUT TIME ZONE",
            nullable = false
    )
    private LocalDateTime lastUpdateDate;
}
