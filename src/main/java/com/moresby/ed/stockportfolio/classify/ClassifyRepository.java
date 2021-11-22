package com.moresby.ed.stockportfolio.classify;

import com.moresby.ed.stockportfolio.tstock.TStock;
import org.hibernate.sql.Select;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import yahoofinance.Stock;

import javax.persistence.NamedNativeQuery;
import java.util.List;

public interface ClassifyRepository extends CrudRepository<Classify, Integer> {

    @Query("SELECT c FROM Classify c WHERE c.name = ?1")
    Classify findClassifyByNameEquals(String classifyName);

    @Query("SELECT ts FROM TStock ts WHERE ts.classify.classifyId=?1")
    List<TStock> findStocksByClassifyId(Integer classifyId);
}
