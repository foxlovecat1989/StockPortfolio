package com.moresby.ed.stockportfolio.repository;


import com.moresby.ed.stockportfolio.domain.Trade;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import java.sql.Date;
import java.util.List;

public interface TradeRepository extends CrudRepository<Trade, Long> {
    @Query(value = "SELECT t FROM Trade t WHERE t.user.id = ?1 AND t.tradeDate = ?2")
    List<Trade> findAllByUserIdAndTradeDateEquals(Long userId, Date tradeDate);
}
