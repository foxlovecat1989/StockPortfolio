package com.moresby.ed.stockportfolio.service.impl;

import com.moresby.ed.stockportfolio.domain.Classify;
import com.moresby.ed.stockportfolio.repository.ClassifyRepository;
import com.moresby.ed.stockportfolio.service.ClassifyService;
import com.moresby.ed.stockportfolio.domain.TStock;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ClassifyServiceImpl implements ClassifyService {

    private final ClassifyRepository classifyRepository;

    @Override
    public Classify createClassify(Classify classify) {
        return classifyRepository.save(classify);
    }
    @Override
    public Classify createClassifyByName(String classifyName) {
        var classify = new Classify();
        classify.setName(classifyName);

        return classifyRepository.save(classify);
    }

    @Override
    public Classify findClassifyByName(String name) {
        return classifyRepository.findClassifyByNameEquals(name);
    }

    @Override
    public List<TStock> findStocksByClassifyId(Integer classifyId) {
        return classifyRepository.findStocksByClassifyId(classifyId);
    }

    @Override
    public Iterable<Classify> findAllClassify() {

        return classifyRepository.findAll();
    }

    @Override
    public Classify updateClassify(Classify classify) {
        Classify originClassify = classifyRepository.findById(classify.getClassifyId())
                .orElseThrow(() -> new IllegalStateException("Classify Id:" + classify.getClassifyId() + "Not Found!" ));
        originClassify.setClassifyId(classify.getClassifyId());
        originClassify.setName(classify.getName() != null ? classify.getName() : originClassify.getName());

        return classifyRepository.save(originClassify);
    }

    @Override
    public void deleteById(Integer classifyId) {
        try{
            classifyRepository.deleteById(classifyId);
        } catch(EmptyResultDataAccessException e) {
            e.printStackTrace();
        }
    }


}
