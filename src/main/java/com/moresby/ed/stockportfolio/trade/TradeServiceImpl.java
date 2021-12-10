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

import javax.swing.text.html.Option;
import javax.transaction.Transactional;
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
        TStock tStock =
                tStockRepository.findById(stockId).orElseThrow(
                        () -> new NoSuchElementException(String.format("Stock Id: %s Not Found", stockId))
                );
        Inventory inventory =
                optInventory.isPresent() ? optInventory.get() : new Inventory();
        Trade trade = new Trade();
        trade.setUser(user);
        trade.setTStock(tStock);
        trade.setPrice(tStock.getPrice().doubleValue());
        trade.setAmount(amount);
        trade.setTradeType(TradeType.BUY);
//        trade.setTradeDate(
//                new java.sql.Date(
//                        new java.util.Date().getTime())
//        );
//        trade.setTradeTime(new Time(new java.util.Date().getTime()));
        // TODO: CHANGE THESE FAKER DATE AND TIME WHEN PRODUCTION
        trade.setTradeDate(
                new java.sql.Date(
                        new java.util.Date().getTime() + MILLISECONDS_IN_ONE_DAY * faker.number().numberBetween(1, 10))
        );
        trade.setTradeTime(java.sql.Time.valueOf(String.format("%d:00:00", faker.number().numberBetween(9, 12))));

        inventory.setUser(user);
        inventory.setTStock(tStock);
        inventory.setAmount(
                amount + (optInventory.isPresent() ? optInventory.get().getAmount() : 0)
        );

        int buyTotalCost = tStock.getPrice().intValue() * amount;
        int remainBalance = user.getBalance() - buyTotalCost;
        // TODO: EXAMINE THE BALANCE IS GREATER THAN ZERO - if (remainBalance < 0) ROLLBACK
        user.setBalance(remainBalance);

        inventoryRepository.save(inventory);
        userRepository.save(user);

        return tradeRepository.save(trade);
    }

    @Override
    public Trade sell(Long userId, Long stockId, Integer amount) {

        return null;
    }
}
