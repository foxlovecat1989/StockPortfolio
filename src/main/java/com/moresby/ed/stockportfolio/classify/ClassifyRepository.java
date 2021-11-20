package com.moresby.ed.stockportfolio.classify;

import org.hibernate.sql.Select;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ClassifyRepository extends CrudRepository<Classify, Integer> {

    @Query("SELECT c FROM Classify c WHERE c.name = ?1")
    Classify findClassifyByNameEquals(String classifyName);
}
