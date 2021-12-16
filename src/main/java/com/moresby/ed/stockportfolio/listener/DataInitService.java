package com.moresby.ed.stockportfolio.listener;

import com.github.javafaker.Faker;
import com.moresby.ed.stockportfolio.domain.*;
import com.moresby.ed.stockportfolio.exception.domain.EmailExistException;
import com.moresby.ed.stockportfolio.exception.domain.UsernameExistException;
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
    public void initData() throws EmailExistException, UsernameExistException {
        generateUsers(TEN_TIMES);
        generateStocks();
        generateExecuteTrades(HUNDRED_TIMES);
        generateWatchlistAndAddRandomStock();
    }

    private void generateUsers(int times) throws EmailExistException, UsernameExistException {
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
           userService.createUser(user);
        }
    }

    private void generateStocks() {
        generateClassify();
        var classifyStock = classifyService.findClassifyByName("Ordinary Stock");
        var classifyForeignExchange = classifyService.findClassifyByName("Foreign Exchange");
        var classifyTWSE = classifyService.findClassifyByName("TWSE");

        Iterable<TStock> tStocks = List.of(
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

        tStockService.createStocks(tStocks);
        tStockService.refreshPriceOfStocks();
    }

    private void generateClassify() {
        // List.of("Ordinary Stock/普通股", "Futures/期貨", "Fund/基金", "Foreign Exchange/外匯")
        Stream.of("Ordinary Stock", "Futures", "Fund", "Foreign Exchange", "TWSE").forEach(classifyService::createClassifyByName);
    }

    private void generateExecuteTrades(int times){
        for (int i = 0; i < times; i++) {
            Long userId = getFakeNumberBetween(1L, 10L);
            Long stockId = getFakeNumberBetween(1L, 10L);
            Long amount = getFakeNumberBetween(1L, 10L) * PER_UNIT_EQUALS_ONE_THOUSAND;
            var user = userService.findExistingUserById(userId);
            var stock = tStockService.findExistingStock(stockId);
            TradePOJO trade =
                    TradePOJO.builder()
                            .user(user)
                            .tStock(stock)
                            .amount(amount)
                            .tradeType(TradeType.BUY)
                            .build();
            try{
                tradeService.executeTrade(trade);
                boolean toSellIt = faker.number().numberBetween(0, 7) % 2 == 0;
                if(toSellIt){
                    trade.setTradeType(TradeType.SELL);
                    tradeService.executeTrade(trade);
                }
            } catch (RuntimeException e){
                System.out.println("Transaction failed");
                System.out.println(e.getMessage());
            }


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
                            tStocks.add(tStockService.findExistingStock(randomStockId));
                        }

                        Watchlist watchlist =
                                Watchlist.builder()
                                .name(user.getUsername() + "'s watchlist "+ (randomAmountOfWatchlist - i) )
                                .user(user)
                                .lastUpdateAt(LocalDateTime.now().minusDays(randomDays))
                                .tStocks(tStocks)
                                .build();

                        watchlistService.createWatch(watchlist);
                    }
                }
        );

    }

    private long getFakeNumberBetween(Long min, Long max) {
        return faker.number().numberBetween(min, max);
    }
}
