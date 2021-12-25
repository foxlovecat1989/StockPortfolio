package com.moresby.ed.stockportfolio.service.impl;

import com.moresby.ed.stockportfolio.domain.Classify;
import com.moresby.ed.stockportfolio.exception.domain.classify.ClassifyNameExistException;
import com.moresby.ed.stockportfolio.exception.domain.classify.ClassifyNotFoundException;
import com.moresby.ed.stockportfolio.repository.ClassifyRepository;
import com.moresby.ed.stockportfolio.service.ClassifyService;
import com.moresby.ed.stockportfolio.domain.TStock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.moresby.ed.stockportfolio.constant.ClassifyImplConstant.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClassifyServiceImpl implements ClassifyService {

    private final ClassifyRepository classifyRepository;

    @Override
    public Classify createClassify(Classify classify) throws ClassifyNameExistException {
        validateClassifyName(classify.getName());

        return classifyRepository.save(classify);
    }

    @Override
    public Classify createClassifyByName(String classifyName) throws ClassifyNameExistException {
        validateClassifyName(classifyName);
        var classify = new Classify();
        classify.setName(classifyName);

        return classifyRepository.save(classify);
    }

    @Override
    public Classify findExistClassifyByName(String classifyName) throws ClassifyNotFoundException {
        return classifyRepository.findClassifyByName(classifyName).orElseThrow(
                () -> {
                    var errorMsg = String.format(CLASSIFY_NOT_FOUND_BY_CLASSIFY_NAME, classifyName);
                    log.error(errorMsg);
                    return new ClassifyNotFoundException(errorMsg);
                }
        );
    }

    @Override
    public Classify findExistClassifyById(Integer classifyId) throws ClassifyNotFoundException {
        return classifyRepository.findById(classifyId).orElseThrow(
                () -> {
                    var errorMsg = String.format(CLASSIFY_NOT_FOUND_BY_CLASSIFY_ID, classifyId);
                    log.error(errorMsg);
                    return new ClassifyNotFoundException(errorMsg);
                }
        );
    }

    @Override
    public List<TStock> findStocksByClassifyName(String name) {
        return classifyRepository.findStocksByClassifyName(name);
    }

    @Override
    public List<Classify> findAllClassify() {

        return classifyRepository.findAll();
    }

    @Override
    public Classify updateClassifyName(Classify classify) throws ClassifyNotFoundException {
        var originClassify = findExistClassifyById(classify.getClassifyId());
        originClassify.setName(classify.getName() != null ? classify.getName() : originClassify.getName());

        return classifyRepository.save(originClassify);
    }

    @Override
    public void deleteByName(String classifyName) throws ClassifyNotFoundException {
         var classify = findExistClassifyByName(classifyName);
         classifyRepository.delete(classify);
    }

    private boolean isClassifyNameBeTaken(String classifyName){
        return classifyRepository.findClassifyByName(classifyName).isPresent();
    }

    private void validateClassifyName(String classifyName) throws ClassifyNameExistException {
        var isNameBeTaken = isClassifyNameBeTaken(classifyName);
        if (isNameBeTaken)
            throw new ClassifyNameExistException(CLASSIFY_NAME_ALREADY_EXISTS);
    }
}
