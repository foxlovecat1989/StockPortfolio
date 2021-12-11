package com.moresby.ed.stockportfolio.trade;

import com.github.javafaker.Faker;
import com.moresby.ed.stockportfolio.inventory.Inventory;
import com.moresby.ed.stockportfolio.inventory.InventoryRepository;
import com.moresby.ed.stockportfolio.inventory.InventoryService;
import com.moresby.ed.stockportfolio.tstock.TStock;
import com.moresby.ed.stockportfolio.tstock.TStockRepository;
import com.moresby.ed.stockportfolio.user.User;
import com.moresby.ed.stockportfolio.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.naming.InsufficientResourcesException;
import javax.swing.text.html.Option;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TradeServiceImpl implements TradeService {

    private final TradeRepository tradeRepository;
    private final InventoryRepository inventoryRepository;
    private final UserRepository userRepository;
    private final TStockRepository tStockRepository;
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
    public Trade buy(Long userId, Long stockId, Integer amount) {
        Optional<Inventory> optInventory = inventoryRepository.findOneByUserIdAndTStockId(userId, stockId);
        User user =
                userRepository.findById(userId)
                        .orElseThrow(
                                () -> new NoSuchElementException(String.format("User Id: %s Not Found", userId))
                        );
        TStock stock =
                tStockRepository.findById(stockId).orElseThrow(
                        () -> new NoSuchElementException(String.format("Stock Id: %s Not Found", stockId))
                );

        Trade trade = new Trade();
        trade.setUser(user);
        trade.setTStock(stock);
        trade.setPrice(stock.getPrice());
        trade.setAmount(BigDecimal.valueOf(amount));
        trade.setTradeType(TradeType.BUY);
//        trade.setTradeDate(
//                new java.sql.Date(
//                        new java.util.Date().getTime())
//        );
//        trade.setTradeTime(new Time(new java.util.Date().getTime()));
        // TODO: CHANGE THESE FAKER DATE AND TIME WHEN PRODUCTION
        trade.setTradeDate(
                new java.sql.Date(
                        new java.util.Date().getTime() - MILLISECONDS_IN_ONE_DAY * faker.number().numberBetween(1, 10))
        );
        trade.setTradeTime(java.sql.Time.valueOf(String.format("%d:00:00", faker.number().numberBetween(9, 12))));

        Inventory inventory =
                optInventory.isPresent() ? optInventory.get() : new Inventory();
        inventory.setUser(user);
        inventory.setTStock(stock);
        inventory.setAmount(
                amount + (optInventory.isPresent() ? optInventory.get().getAmount() : 0)
        );

        long buyTotalCost = Math.round(stock.getPrice().doubleValue() * amount);
        double remainBalance = user.getBalance() - buyTotalCost;
        // TODO: EXAMINE THE BALANCE IS GREATER THAN ZERO - if (remainBalance < 0) ROLLBACK
        user.setBalance(remainBalance);

        double avgPrice = calculateAvgPriceInInventory(amount, stock, inventory);
        inventory.setAvgPrice(BigDecimal.valueOf(avgPrice));

        inventoryRepository.save(inventory);
        userRepository.save(user);

        return tradeRepository.save(trade);
    }


    @Override
    @Transactional
    public Trade sell(Long userId, Long stockId, Integer amount) {
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

    private double calculateAvgPriceInInventory(Integer amount, TStock stock, Inventory inventory) {
        if(inventory.getId() == null)
            return stock.getPrice().doubleValue();

        return (inventory.getAvgPrice().doubleValue() * inventory.getAmount() + stock.getPrice().doubleValue() * amount) / (inventory.getAmount() + amount);
    }
}
