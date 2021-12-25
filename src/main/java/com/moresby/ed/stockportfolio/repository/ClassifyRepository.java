package com.moresby.ed.stockportfolio.repository;

import com.moresby.ed.stockportfolio.domain.Classify;
import com.moresby.ed.stockportfolio.domain.TStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ClassifyRepository extends JpaRepository<Classify, Integer> {

    @Query("SELECT c FROM Classify c WHERE c.name = ?1")
    Optional<Classify> findClassifyByName(String classifyName);

    @Query("SELECT ts FROM TStock ts WHERE ts.classify.name=?1")
    List<TStock> findStocksByClassifyName(String name);
}
