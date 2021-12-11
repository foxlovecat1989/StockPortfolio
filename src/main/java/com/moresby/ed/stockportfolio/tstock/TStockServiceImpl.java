package com.moresby.ed.stockportfolio.tstock;

import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class TStockServiceImpl implements TStockService {

    private final TStockRepository tStockRepository;


    @Override
    public Iterable<TStock> findAllStocks() {
        return tStockRepository.findAll();
    }

    @Override
    public Optional<TStock> findStock(Long id) {
        return tStockRepository.findById(id);
    }

    @Override
    public List<TStock> createStocks(Iterable<TStock> newTStocks) {
        List<TStock> tStocks = new ArrayList<>();
        newTStocks.forEach(tStock -> tStocks.add(createStock(tStock)));

        return tStocks;
    }


    @Override
    public TStock createStock(TStock TStock) {
        return tStockRepository.save(TStock);
    }

    @Override
    public TStock updateStock(TStock tStock) {
        TStock originTStock =
                tStockRepository.findById(tStock.getId())
                .orElseThrow(() -> new IllegalStateException("Stock Id:" + tStock.getId() + "Not Found!" ));

        originTStock.setName(
                tStock.getName() != null ? tStock.getName() : originTStock.getName()
        );
        originTStock.setSymbol(
                tStock.getSymbol()!= null ? tStock.getSymbol() : originTStock.getSymbol()
        );
        originTStock.setClassify(
                tStock.getClassify()!= null ? tStock.getClassify() : originTStock.getClassify()
        );
        tStockRepository.save(originTStock);

        return originTStock;
    }

    @Override
    public void deleteStock(Long id) {
        try{
            tStockRepository.deleteById(id);
        }catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<TStock> refreshPriceOfStocks() {
        List<TStock> tStocks = StreamSupport
                .stream(this.findAllStocks().spliterator(), false)
                .collect(Collectors.toList());
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
                // Update price
                // Thread.sleep(1000);
                this.updatePrice(
                        tStock.getId(),
                        tStock.getChangePrice(),
                        tStock.getChangeInPercent(),
                        tStock.getPreviousClosed(),
                        tStock.getPrice(),
                        tStock.getLastUpDateTime(),
                        tStock.getVolume()
                );

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return tStocks;
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
    public TStock findExistingStock(Long stockId) {

        return  tStockRepository.findById(stockId).orElseThrow(
                        () -> new NoSuchElementException(String.format("Stock Id: %s Not Found", stockId))
                );
    }


}
