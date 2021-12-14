package com.moresby.ed.stockportfolio.repository;

import com.moresby.ed.stockportfolio.domain.TStock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;


public interface TStockRepository extends CrudRepository<TStock, Long> {

//    @Transactional
//    @Modifying
//    @Query(value = "UPDATE TStock s SET s.name=?2, s.symbol=?3 WHERE s.id=?1")
//    public void update(
//            @Param("id") Integer id,
//            @Param("name") String name,
//            @Param("symbol") String symbol
//    );

    @Transactional
    @Modifying
    @Query(value =
            "UPDATE TStock s " +
            "SET " +
                "s.changePrice=?2, " +
                "s.changeInPercent=?3, " +
                "s.previousClosed=?4, " +
                "s.price=?5, " +
                "s.lastUpDateTime=?6, " +
                "s.volume=?7 " +
            "WHERE s.id=?1")
    void updatePrice(
            @Param("id") Long id,
            @Param("changePrice") BigDecimal changePrice,
            @Param("changeInPercent") BigDecimal changeInPercent,
            @Param("previousClosed") BigDecimal previousClosed,
            @Param("price") BigDecimal price,
            @Param("lastUpDateTime") LocalDateTime lastUpDateTime,
            @Param("volume") Long volume
    );
}
