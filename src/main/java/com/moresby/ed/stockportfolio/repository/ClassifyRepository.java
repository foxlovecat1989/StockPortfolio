package com.moresby.ed.stockportfolio.repository;

import com.moresby.ed.stockportfolio.domain.Classify;
import com.moresby.ed.stockportfolio.domain.TStock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ClassifyRepository extends CrudRepository<Classify, Integer> {

    @Query("SELECT c FROM Classify c WHERE c.name = ?1")
    Classify findClassifyByNameEquals(String classifyName);

    @Query("SELECT ts FROM TStock ts WHERE ts.classify.classifyId=?1")
    List<TStock> findStocksByClassifyId(Integer classifyId);
}
