package com.moresby.ed.stockportfolio.service.impl;

import com.moresby.ed.stockportfolio.domain.TStock;
import com.moresby.ed.stockportfolio.exception.domain.stock.StockExistException;
import com.moresby.ed.stockportfolio.exception.domain.stock.StockNotfoundException;
import com.moresby.ed.stockportfolio.repository.TStockRepository;
import com.moresby.ed.stockportfolio.service.TStockService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import static com.moresby.ed.stockportfolio.constant.StockImplConstant.*;

@Service
@AllArgsConstructor
@Slf4j
public class TStockServiceImpl implements TStockService {

    private final TStockRepository tStockRepository;

    @Override
    public TStock findExistingStockById(Long id) throws StockNotfoundException {
        return tStockRepository.findById(id).orElseThrow(
                () -> {
                    var errorMsg = String.format(NO_STOCK_FOUND_BY_ID, id);
                    log.error(errorMsg);
                    return new StockNotfoundException(errorMsg);
                }
        );
    }

    @Override
    public List<TStock> findAllStocks() {

        return tStockRepository.findAll();
    }

    @Override
    public TStock findExistingStockBySymbol(String symbol) throws StockNotfoundException {

        return tStockRepository.findTStockBySymbol(symbol).orElseThrow(
                () -> {
                    var errorMsg = String.format(NO_STOCK_FOUND_BY_SYMBOL, symbol);
                    log.error(errorMsg);
                    return new StockNotfoundException(errorMsg);
                }
        );
    }

    @Override
    public TStock createStock(TStock tStock) throws StockExistException {
        checkIsStockAlreadyExist(tStock);

        return tStockRepository.save(tStock);
    }

    @Override
    public TStock updateStock(TStock tStock) throws StockNotfoundException {
        TStock originTStock = findExistingStockBySymbol(tStock.getSymbol());
        originTStock.setName(
                tStock.getName() != null ? tStock.getName() : originTStock.getName()
        );
        originTStock.setSymbol(
                tStock.getSymbol()!= null ? tStock.getSymbol() : originTStock.getSymbol()
        );
        originTStock.setClassify(
                tStock.getClassify()!= null ? tStock.getClassify() : originTStock.getClassify()
        );

        return  tStockRepository.save(originTStock);
    }

    @Override
    public void updatePrice(
            Long tStockId,
            BigDecimal changePrice,
            BigDecimal changeInPercent,
            BigDecimal previousClosed,
            BigDecimal price,
            LocalDateTime transactionDate,
            Long volume
    ) {
        tStockRepository.updatePrice(tStockId, changePrice, changeInPercent, previousClosed, price, transactionDate, volume);
    }

    @Override
    public List<TStock> refreshPriceOfStocks() {
        List<TStock> tStocks = findAllStocks();
        for (TStock tStock : tStocks) {
            // retrieve stock information from yahoo finance
            try {
                Stock stock = YahooFinance.get(tStock.getSymbol());
                tStock.setChangePrice(stock.getQuote().getChange());
                tStock.setChangeInPercent(stock.getQuote().getChangeInPercent());
                tStock.setPreviousClosed(stock.getQuote().getPreviousClose());
                tStock.setPrice(stock.getQuote().getPrice());
                var date = stock.getQuote().getLastTradeTime().getTime();
                var instant = Instant.ofEpochMilli(date.getTime());
                LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
                tStock.setLastUpDateTime(localDateTime);
                tStock.setVolume(stock.getQuote().getVolume());
                this.updatePrice(
                        tStock.getId(),
                        tStock.getChangePrice(),
                        tStock.getChangeInPercent(),
                        tStock.getPreviousClosed(),
                        tStock.getPrice(),
                        tStock.getLastUpDateTime(),
                        tStock.getVolume()
                );
                log.info(String.format("Updating the stock price at %s", LocalDateTime.now()));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return tStocks;
    }

    private boolean isStockAlreadyExist(TStock tStock){
        Optional<TStock> optTStock = tStockRepository.findTStockBySymbol(tStock.getSymbol());

        return optTStock.isPresent();
    }

    private void checkIsStockAlreadyExist(TStock tStock) throws StockExistException {
        boolean isStockAlreadyExist = isStockAlreadyExist(tStock);
        if(isStockAlreadyExist){
            var errorMsg = String.format(STOCK_ALREADY_EXISTS, tStock.getSymbol());
            log.warn(errorMsg);
            throw new StockExistException(errorMsg);
        }

    }

}
