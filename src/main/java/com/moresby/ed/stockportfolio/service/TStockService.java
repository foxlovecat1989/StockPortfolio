package com.moresby.ed.stockportfolio.service;

import com.moresby.ed.stockportfolio.domain.TStock;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

public interface TStockService {
    Iterable<TStock> findAllStocks();
    Optional<TStock> findStock(Long id);
    Iterable<TStock> createStocks(Iterable<TStock> stocks);
    TStock createStock(TStock TStock);
    TStock updateStock(TStock TStock);
    void deleteStock(Long id);
    Iterable<TStock> refreshPriceOfStocks();
    void updatePrice(
            Long tStockId,
            BigDecimal changePrice,
            BigDecimal changeInPercent,
            BigDecimal preClosed,
            BigDecimal price,
            LocalDateTime lastUpDateTime,
            Long volume
    );
    TStock findExistingStock(Long stockId);
}
