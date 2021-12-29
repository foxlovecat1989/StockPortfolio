package com.moresby.ed.stockportfolio.service;

import com.moresby.ed.stockportfolio.domain.TStock;
import com.moresby.ed.stockportfolio.exception.domain.stock.ConnectErrorException;
import com.moresby.ed.stockportfolio.exception.domain.stock.StockExistException;
import com.moresby.ed.stockportfolio.exception.domain.stock.StockNotfoundException;
import yahoofinance.histquotes.HistoricalQuote;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface TStockService {
    List<TStock> findAllStocks();
    TStock findExistingStockById(Long id) throws StockNotfoundException;
    TStock findExistingStockByName(String name) throws StockNotfoundException;
    TStock createStock(TStock TStock) throws StockExistException;
    TStock updateStock(TStock TStock) throws StockNotfoundException, StockExistException;

    void refreshPriceOfStocks();

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

    List<HistoricalQuote> getHistoricalQuotes(String symbol, Integer month) throws ConnectErrorException;
}
