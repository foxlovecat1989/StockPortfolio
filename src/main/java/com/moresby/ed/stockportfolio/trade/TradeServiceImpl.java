package com.moresby.ed.stockportfolio.trade;

import com.github.javafaker.Faker;
import com.moresby.ed.stockportfolio.inventory.InventoryService;
import com.moresby.ed.stockportfolio.trade.model.entity.Trade;
import com.moresby.ed.stockportfolio.trade.model.pojo.TradePOJO;
import com.moresby.ed.stockportfolio.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TradeServiceImpl implements TradeService {

    private final TradeRepository tradeRepository;
    private final UserRepository userRepository;
    private final InventoryService inventoryService;
    private final Faker faker;
    private static final int MILLISECONDS_IN_ONE_DAY = 86400 * 1000;

    @Override
    public Optional<Trade> findByTradeId(Long tradeId) {
        return tradeRepository.findById(tradeId);
    }

    @Override
    public List<Trade> findOneByUserIdAndTradeDate(Long userId, Date tradeDate) {
        return tradeRepository.findAllByUserIdAndTradeDateEquals(userId, tradeDate);
    }

    @Override
    public Iterable<Trade> findAll() {
        return tradeRepository.findAll();
    }

    @Override
    @Transactional
    public Trade buy(TradePOJO tradePOJO) {

        inventoryService.updateInventory(tradePOJO);

        // TODO: REMOVE THIS TO ACCOUNT SERVICE
        var user = tradePOJO.getUser();
        var stock = tradePOJO.getTStock();
        var amount = tradePOJO.getAmount();
        long buyTotalCost = Math.round(stock.getPrice().doubleValue() * amount);
        double remainBalance = user.getBalance() - buyTotalCost;
        // TODO: EXAMINE THE BALANCE IS GREATER THAN ZERO - if (remainBalance < 0) ROLLBACK
        user.setBalance(remainBalance);
        userRepository.save(user);

        return createTrade(tradePOJO);
    }

    @Override
    @Transactional
    public Trade sell(TradePOJO tradePOJO) {
//       Inventory inventory = inventoryRepository.findOneByUserIdAndTStockId(userId, stockId)
//               .orElseThrow(
//                       () -> new NoSuchElementException(String.format("No Stock ID: %s in your inventory", stockId))
//               );
//        User user =
//                userRepository.findById(userId)
//                        .orElseThrow(
//                                () -> new NoSuchElementException(String.format("User Id: %s Not Found", userId))
//                        );
//        TStock stock =
//                tStockRepository.findById(stockId).orElseThrow(
//                        () -> new NoSuchElementException(String.format("Stock Id: %s Not Found", stockId))
//                );
//
//        // TODO: EXAMINE THE AMOUNT IN INVENTORY IS GREATER THAN SOLD AMOUNT - if(inventory.getAmount() - amount < 0) Rollback
//        inventory.setUser(user);
//        inventory.setTStock(stock);
//        inventory.setAmount(inventory.getAmount() - amount);
//
//        Trade trade = new Trade();
//        trade.setUser(user);
//        trade.setTStock(stock);
//        // TODO: CHANGE THESE FAKER PRICE WHEN PRODUCTION
//        trade.setPrice(BigDecimal.valueOf(stock.getPrice().doubleValue() + faker.number().numberBetween(-10, 10)));
//        // trade.setPrice(stock.getPrice());
//        trade.setAmount(BigDecimal.valueOf(amount));
//        trade.setTradeType(TradeType.SELL);
////        trade.setTradeDate(
////                new java.sql.Date(
////                        new java.util.Date().getTime())
////        );
////        trade.setTradeTime(new Time(new java.util.Date().getTime()));
//        // TODO: CHANGE THESE FAKER DATE AND TIME WHEN PRODUCTION
//        trade.setTradeDate(
//                new java.sql.Date(
//                        new java.util.Date().getTime() + MILLISECONDS_IN_ONE_DAY * faker.number().numberBetween(1, 10))
//        );
//        trade.setTradeTime(java.sql.Time.valueOf(String.format("%d:00:00", faker.number().numberBetween(9, 12))));
//
//        long income = Math.round(stock.getPrice().doubleValue() * amount);
//        double remainBalance = user.getBalance() + income;
//        user.setBalance(remainBalance);
//        inventory.setCost();


        return null;
    }

    private Trade createTrade(TradePOJO tradePOJO) {
        Trade trade = new Trade();
        trade.setUser(tradePOJO.getUser());
        trade.setTStock(tradePOJO.getTStock());
        trade.setPrice(tradePOJO.getTStock().getPrice());
        trade.setAmount(BigDecimal.valueOf(tradePOJO.getAmount()));
        trade.setTradeType(tradePOJO.getTradeType());
//        trade.setTradeDate(
//                new java.sql.Date(
//                        new java.util.Date().getTime())
//        );
//        trade.setTradeTime(new Time(new java.util.Date().getTime()));
        // TODO: CHANGE THESE FAKER DATE AND TIME WHEN PRODUCTION
        trade.setTradeDate(
                new Date(
                        new java.util.Date().getTime() - MILLISECONDS_IN_ONE_DAY * faker.number().numberBetween(1, 10))
        );
        trade.setTradeTime(Time.valueOf(String.format("%d:00:00", faker.number().numberBetween(9, 12))));
        tradeRepository.save(trade);

        return trade;
    }
}
