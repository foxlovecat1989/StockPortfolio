package com.moresby.ed.stockportfolio.service;

import com.github.javafaker.Faker;
import com.moresby.ed.stockportfolio.activity.Activity;
import com.moresby.ed.stockportfolio.activity.ActivityService;
import com.moresby.ed.stockportfolio.activity.activitytype.ActivityType;
import com.moresby.ed.stockportfolio.trade.Trade;
import com.moresby.ed.stockportfolio.trade.TradeService;
import com.moresby.ed.stockportfolio.user.User;
import com.moresby.ed.stockportfolio.classify.ClassifyService;
import com.moresby.ed.stockportfolio.user.UserService;
import com.moresby.ed.stockportfolio.watchlist.WatchlistId;
import com.moresby.ed.stockportfolio.tstock.TStock;
import com.moresby.ed.stockportfolio.tstock.TStockService;
import com.moresby.ed.stockportfolio.watch.Watch;
import com.moresby.ed.stockportfolio.watch.WatchService;
import com.moresby.ed.stockportfolio.watchlist.Watchlist;
import com.moresby.ed.stockportfolio.watchlist.WatchlistService;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class DataInitService {

    private final ClassifyService classifyService;
    private final TStockService tStockService;
    private final UserService userService;
    private final WatchService watchService;
    private final WatchlistService watchlistService;
    private final ActivityService activityService;
    private final TradeService tradeService;
    private Faker faker;
    private static final int TEN_TIMES = 10;
    private static final int HUNDRED_TIMES = 100;
    private static final long MILLISECONDS_IN_ONE_DAY = 86400*1000L;
    private static final int PER_UNIT_EQUALS_ONE_THOUSAND = 1000;
    private static final int ONE_MILLION = 1000000;

    @EventListener(ApplicationReadyEvent.class)
    public void initData() {
        generatedUsers(TEN_TIMES);
        generateClassify();
        generateStocks();
        generateRandomWatch(TEN_TIMES);
        addRandomStockToWatchlist(TEN_TIMES);
        generateActivities();
        generateBuyTrades(TEN_TIMES);
    }

    private void generateBuyTrades(int times){
        for (int i = 0; i < times; i++) {
            Long userId = getFakeNumberBetween(1L, 10L);
            Long stockId = getFakeNumberBetween(1L, 10L);
            Integer amount = (int) getFakeNumberBetween(1L, 10L) * PER_UNIT_EQUALS_ONE_THOUSAND;
            tradeService.buy(userId, stockId, amount);
        }
    }

    private void generateActivities() {
        generateActivity("Stock Course for Beginners", "Online Course", ActivityType.COURSE);
        generateActivity("Hank McClure's Speech", "Civic Activity Center", ActivityType.SPEECH);
        generateActivity("Stock Course for Advance", "Online Course", ActivityType.COURSE);
        generateActivity("Stock Discussion", "Online channel", ActivityType.DISCUSSION);
    }

    private void generateActivity(String name, String location, ActivityType type) {
        var activity = new Activity();
        activity.setName(name);
        activity.setLocation(location);
        activity.setStartDate(
                new java.sql.Date(
                        new java.util.Date().getTime() + MILLISECONDS_IN_ONE_DAY * faker.number().numberBetween(1, 10)
                )
        );
        activity.setStartTime(java.sql.Time.valueOf(String.format("%d:00:00", faker.number().numberBetween(8, 14))));
        activity.setEndTime(java.sql.Time.valueOf(String.format("%d:30:00", faker.number().numberBetween(14, 20))));
        activity.setActivityType(type);
        activity.setLimitAmount(faker.number().numberBetween(1, 100));
        activityService.create(activity);
    }

    private Date convertToDateViaInstant(LocalDateTime dateToConvert) {
        return java.util.Date
                .from(dateToConvert.atZone(ZoneId.systemDefault())
                        .toInstant());
    }

    private void generateClassify() {
        // List.of("Ordinary Stock/普通股", "Futures/期貨", "Fund/基金", "Foreign Exchange/外匯")
        Stream.of("Ordinary Stock", "Futures", "Fund", "Foreign Exchange", "TWSE").forEach(classifyService::createClassifyByName);
    }

    private void generateStocks() {
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
        this.getPriceOfStock();
    }

    private void getPriceOfStock() {
        tStockService.refreshPriceOfStocks();
    }

    private void generatedUsers(Integer times) {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < times; i++) {
            User user = generateRandomUser();
            users.add(user);
        }

        userService.createUsers(users);
    }

    private User generateRandomUser(){
        User user = new User();
        String username = faker.name().lastName();
        String password = Integer.toHexString(user.hashCode());
        String email = String.format("%s@gmail.com", username);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setBalance((double)(getFakeNumberBetween(5L, 10L) * ONE_MILLION));

        return user;
    }

    private void generateRandomWatch(Integer times) {
        for (int i = 0; i < times; i++) {
            Long fakeUserId = getFakeNumberBetween(1L, 10L);
            String fakeWatchName = faker.name().nameWithMiddle().toUpperCase();
            Optional<User> optUser = userService.findUserById(fakeUserId);
            optUser.ifPresent(user -> addWatch(fakeWatchName, user));
        }
    }

    private Watch addWatch(String watchName, User user){
        Watch watch = new Watch();
        watch.setName(watchName);
        watch.setUser(user);

        return  watchService.createWatch(watch);
    }

    private long getFakeNumberBetween(Long min, Long max) {
        return faker.number().numberBetween(min, max);
    }

    private void addRandomStockToWatchlist(Integer times) {
        for (int i = 0; i <	times ; i++) {
            var fakeTStockId = getFakeNumberBetween(1L, 10L);
            var fakeWatchId = getFakeNumberBetween(1L, 10L);
            TStock tStock = tStockService.findStock(fakeTStockId).get();
            Watch watch = watchService.findOneById(fakeWatchId).get();
            addStockToWatchlist(tStock, watch);
        }
    }

    private Watchlist addStockToWatchlist(TStock tStock, Watch watch){
        Watchlist watchlist = new Watchlist();
        WatchlistId watchlistId = new WatchlistId();
        watchlistId.setWatchId(watch.getId());
        watchlistId.setTStockId(tStock.getId());
        watchlist.setWatchlistId(watchlistId);
        watchlist.setTStock(tStock);
        watchlist.setWatch(watch);
        watchlist.setLastUpdateDate(LocalDateTime.now());

        return watchlistService.createOne(watchlist);
    }
}
