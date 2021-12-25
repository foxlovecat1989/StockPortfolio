package com.moresby.ed.stockportfolio.service;

import com.moresby.ed.stockportfolio.domain.Classify;
import com.moresby.ed.stockportfolio.domain.TStock;
import com.moresby.ed.stockportfolio.exception.domain.classify.ClassifyNameExistException;
import com.moresby.ed.stockportfolio.exception.domain.classify.ClassifyNotFoundException;

import java.util.List;

public interface ClassifyService {

    Classify createClassify(Classify classify) throws ClassifyNameExistException;
    Classify createClassifyByName(String classifyName) throws ClassifyNameExistException;
    Classify findExistClassifyByName(String name) throws ClassifyNotFoundException;
    Classify findExistClassifyById(Integer classifyId) throws ClassifyNotFoundException;
    List<TStock> findStocksByClassifyName(String name);
    List<Classify> findAllClassify();
    Classify updateClassifyName(Classify classify) throws ClassifyNotFoundException;

    void deleteByName(String classifyName) throws ClassifyNotFoundException;
}
