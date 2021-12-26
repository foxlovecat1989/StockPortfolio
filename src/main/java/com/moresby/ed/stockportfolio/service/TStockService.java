package com.moresby.ed.stockportfolio.service;

import com.moresby.ed.stockportfolio.domain.TStock;
import com.moresby.ed.stockportfolio.exception.domain.stock.StockExistException;
import com.moresby.ed.stockportfolio.exception.domain.stock.StockNotfoundException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TStockService {
    List<TStock> findAllStocks();
    TStock findExistingStockById(Long id) throws StockNotfoundException;
    TStock findExistingStockByName(String name) throws StockNotfoundException;
    TStock createStock(TStock TStock) throws StockExistException;
    TStock updateStock(TStock TStock) throws StockNotfoundException, StockExistException;

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

    TStock findExistingStockBySymbol(String symbol) throws StockNotfoundException;
}
