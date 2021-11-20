package com.moresby.ed.stockportfolio.classify;

import java.util.Optional;

public interface ClassifyService {
    // C
    Classify createClassify(Classify classify);
    Classify createClassifyByName(String classifyName);
    // R
    Optional<Classify> findClassifyById(Integer classifyId);
    Classify findClassifyByName(String classifyName);
    Iterable<Classify> findAllClassify();
    //U
    Classify updateClassify(Classify classify);
    // D
    void deleteById(Integer classifyId);
}
