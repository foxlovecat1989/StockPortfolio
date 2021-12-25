package com.moresby.ed.stockportfolio.listener;

import com.github.javafaker.Faker;
import com.moresby.ed.stockportfolio.domain.*;
import com.moresby.ed.stockportfolio.exception.domain.classify.ClassifyNameExistException;
import com.moresby.ed.stockportfolio.exception.domain.classify.ClassifyNotFoundException;
import com.moresby.ed.stockportfolio.exception.domain.stock.StockExistException;
import com.moresby.ed.stockportfolio.service.*;
import com.moresby.ed.stockportfolio.enumeration.TradeType;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class DataInitService {

    private final ClassifyService classifyService;
    private final TStockService tStockService;
    private final UserService userService;
    private final WatchlistService watchlistService;
    private final TradeService tradeService;
    private Faker faker;
    private static final int TEN_TIMES = 10;
    private static final int HUNDRED_TIMES = 100;
    private static final int PER_UNIT_EQUALS_ONE_THOUSAND = 1000;

    @EventListener(ApplicationReadyEvent.class)
    public void initData() {
//        generateUsers(TEN_TIMES);
//        generateStocks();
//        try {
//            generateExecuteTrades(HUNDRED_TIMES);
//        } catch (Exception e) {
//            e.getMessage();
//        }
//        generateWatchlistAndAddRandomStock();
    }

    private void generateUsers(int times){
        for (int i = 0; i < times; i++) {
            boolean isEmailBeTaken;
            String username;
            String email;
            do{
                username = faker.name().lastName();
                email = String.format("%s@gmail.com", username);
                isEmailBeTaken = userService.isEmailTaken(email);
            } while (isEmailBeTaken);
            var user =
                    User.builder()
                            .username(username)
                            .email(email)
                            .password("password")
                            .build();
            try {
                userService.createUser(user);
            } catch (Exception e) {
                e.getMessage();
            }
        }
    }

    private void generateStocks() {
        generateClassify();
        Classify classifyStock = null;
        Classify classifyForeignExchange = null;
        Classify classifyTWSE = null;
        try {
            classifyStock = classifyService.findExistClassifyByName("Ordinary Stock");
             classifyForeignExchange = classifyService.findExistClassifyByName("Foreign Exchange");
            classifyTWSE = classifyService.findExistClassifyByName("TWSE");
        } catch (ClassifyNotFoundException e) {
            e.printStackTrace();
        }

        List<TStock> tStocks = List.of(
                new TStock("2303.TW", "聯電", classifyStock),
                new TStock("2317.TW", "鴻海", classifyStock),
                new TStock("4938.TW", "和碩", classifyStock),
                new TStock("2308.TW", "台達電", classifyStock),
                new TStock("2603.TW", "長榮海", classifyStock),
                new TStock("3711.TW", "日月光控股", classifyStock),
                new TStock("1101.TW", "台泥", classifyStock),
                new TStock("8104.TW", "錸寶", classifyStock),
                new TStock("0050.TW", "元大台灣", classifyStock),
                new TStock("3380.TW", "明泰", classifyStock),
                new TStock("TWDUSD=x", "台幣對美金", classifyForeignExchange),
                new TStock("CNYTWD=x", "人民幣對台幣", classifyForeignExchange),
                new TStock("^TWII", "台灣加權指數", classifyTWSE)
        );
        createStocks(tStocks);
        tStockService.refreshPriceOfStocks();
    }

    private void createStocks(List<TStock> tStocks){
        tStocks
                .forEach(tStock -> {
                    try {
                        tStockService.createStock(tStock);
                    } catch (StockExistException e) {
                       e.getMessage();
                    }
                });
    }

    private void generateClassify() {
        // List.of("Ordinary Stock/普通股", "Futures/期貨", "Fund/基金", "Foreign Exchange/外匯")
        Stream.of("Ordinary Stock", "Futures", "Fund", "Foreign Exchange", "TWSE")
                .forEach(next -> {
                    try {
                        classifyService.createClassifyByName(next);
                    } catch (ClassifyNameExistException e) {
                        e.printStackTrace();
                    }
                });
    }

    private void generateExecuteTrades(int times) {
        for (int i = 0; i < times; i++) {
            Long userId = getFakeNumberBetween(1L, 10L);
            Long stockId = getFakeNumberBetween(1L, 10L);
            Long amount = getFakeNumberBetween(1L, 10L) * PER_UNIT_EQUALS_ONE_THOUSAND;
            User user;
            try {
                user = userService.findExistingUserById(userId);

            var stock = tStockService.findExistingStockById(stockId);
            TradePOJO trade =
                    TradePOJO.builder()
                            .user(user)
                            .tStock(stock)
                            .amount(amount)
                            .tradeType(TradeType.BUY)
                            .build();
                tradeService.executeTrade(trade);
                boolean toSellIt = faker.number().numberBetween(0, 7) % 2 == 0;
                if(toSellIt){
                    trade.setTradeType(TradeType.SELL);
                    tradeService.executeTrade(trade);
                }
            } catch (Exception e){
                e.getMessage();}
            }
    }

    private void generateWatchlistAndAddRandomStock(){
        List<User> users = userService.findAllUsers();
        users.forEach(
                user -> {
                    var randomAmountOfWatchlist = (int) getFakeNumberBetween(1L, 10L);
                    var randomTimesOfAddStock = (int) getFakeNumberBetween(1L, 10L);
                    var randomDays = getFakeNumberBetween(1L, 10L);

                    for (int i = 0; i < randomAmountOfWatchlist; i++) {
                        List<TStock> tStocks = new ArrayList<>();
                        for (int j = 0; j < randomTimesOfAddStock; j++) {
                            var randomStockId = getFakeNumberBetween(1L, 10L);
                            try {
                                tStocks.add(tStockService.findExistingStockById(randomStockId));
                            } catch (Exception e) {
                                e.getMessage();
                            }
                        }

                        Watchlist watchlist =
                                Watchlist.builder()
                                .name(user.getUsername() + "'s watchlist "+ (randomAmountOfWatchlist - i) )
                                .user(user)
                                .lastUpdateAt(LocalDateTime.now().minusDays(randomDays))
                                .tStocks(tStocks)
                                .build();

                        watchlistService.create(watchlist);
                    }
                }
        );

    }

    private long getFakeNumberBetween(Long min, Long max) {
        return faker.number().numberBetween(min, max);
    }
}
