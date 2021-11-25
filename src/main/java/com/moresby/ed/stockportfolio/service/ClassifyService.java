package com.moresby.ed.stockportfolio.service;

import com.moresby.ed.stockportfolio.model.entities.Classify;
import com.moresby.ed.stockportfolio.model.entities.TStock;

import java.util.List;

public interface ClassifyService {
    // C
    Classify createClassify(Classify classify);
    Classify createClassifyByName(String classifyName);
    // R
    Classify findClassifyByName(String name);
    List<TStock> findStocksByClassifyId(Integer classifyId);
    Iterable<Classify> findAllClassify();
    //U
    Classify updateClassify(Classify classify);
    // D
    void deleteById(Integer classifyId);
}
