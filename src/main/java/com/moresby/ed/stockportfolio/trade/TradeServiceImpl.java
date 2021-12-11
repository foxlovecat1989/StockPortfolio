package com.moresby.ed.stockportfolio.trade;

import com.github.javafaker.Faker;
import com.moresby.ed.stockportfolio.inventory.InventoryService;
import com.moresby.ed.stockportfolio.trade.model.entity.Trade;
import com.moresby.ed.stockportfolio.trade.model.enumeration.TradeType;
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
        inventoryService.updateInventory(tradePOJO);

        // TODO: REMOVE THIS TO ACCOUNT SERVICE
        var user = tradePOJO.getUser();
        var stock = tradePOJO.getTStock();
        var amount = tradePOJO.getAmount();
        long profit = Math.round(stock.getPrice().doubleValue() * amount);
        double remainBalance = user.getBalance() + profit;
        user.setBalance(remainBalance);
        userRepository.save(user);

        return createTrade(tradePOJO);
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
        Integer fakeTime = MILLISECONDS_IN_ONE_DAY * faker.number().numberBetween(1, 10);
        Date tradeDate =
                new Date(
                        new java.util.Date().getTime()
                                + (tradePOJO.getTradeType() == TradeType.BUY ? fakeTime : -fakeTime));

        trade.setTradeDate(tradeDate);
        trade.setTradeTime(Time.valueOf(String.format("%d:00:00", faker.number().numberBetween(9, 12))));
        tradeRepository.save(trade);

        return trade;
    }
}
