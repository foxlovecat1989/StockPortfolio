package com.moresby.ed.stockportfolio.repository;


import com.moresby.ed.stockportfolio.domain.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.sql.Date;
import java.util.List;

public interface TradeRepository extends JpaRepository<Trade, Long> {
    @Query(value = "SELECT t FROM Trade t WHERE t.user.userNumber = ?1 AND t.tradeDate = ?2")
    List<Trade> findAllByUserNumberAndTradeDateEquals(String userNumber, Date tradeDate);
}
