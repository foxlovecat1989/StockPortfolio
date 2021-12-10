package com.moresby.ed.stockportfolio.watchlist;

import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
public class WatchlistId implements Serializable {

    @Column(name = "tstock_id")
    private Long tStockId;

    @Column(name = "watch_id")
    private Long watchId;
}