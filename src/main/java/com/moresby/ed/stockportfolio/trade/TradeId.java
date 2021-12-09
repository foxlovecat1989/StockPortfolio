package com.moresby.ed.stockportfolio.trade;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(value = {"userId", "tStockId"})
public class TradeId implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "tStock_id")
    private Long tStockId;
}
