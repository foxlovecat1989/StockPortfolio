package com.moresby.ed.stockportfolio.repository;


import com.moresby.ed.stockportfolio.domain.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.sql.Date;
import java.util.List;

public interface TradeRepository extends JpaRepository<Trade, Long> {
    @Query(value = "SELECT t FROM Trade t WHERE t.user.id = ?1 AND t.tradeDate = ?2")
    List<Trade> findAllByUserIdAndTradeDateEquals(Long userId, Date tradeDate);

    @Query(value = "SELECT t FROM Trade t WHERE t.user.id =?1 AND t.tradeDate BETWEEN ?2 AND ?3")
    List<Trade> findAllTradesByUserIdAndTradeDateBetween(Long userId, Date startDate, Date endDate);
}
