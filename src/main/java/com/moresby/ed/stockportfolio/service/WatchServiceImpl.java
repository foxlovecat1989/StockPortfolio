package com.moresby.ed.stockportfolio.service;

import com.moresby.ed.stockportfolio.model.entities.Watch;
import com.moresby.ed.stockportfolio.repo.WatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WatchServiceImpl implements WatchService {

    private final WatchRepository watchRepository;

    @Override
    public Watch createWatch(Watch watch) {
        return watchRepository.save(watch);
    }

    @Override
    public Optional<Watch> findOneById(Long id) {
        return watchRepository.findById(id);
    }

    @Override
    public Iterable<Watch> findAllByUserId(Long userId) {
        return watchRepository.findAllByUserIdEqual(userId);
    }

    @Override
    public Iterable<Watch> findAll() {
        return watchRepository.findAll();
    }

    @Override
    public Watch updateWatch(Watch watch) {
        var originWatch = watchRepository.findById(watch.getId()).orElseThrow(() -> new IllegalStateException("update watch fail exception"));
        originWatch.setName(watch.getName() != null ? watch.getName() : originWatch.getName());

        return watchRepository.save(originWatch);
    }

    @Override
    public void deleteById(Long id) {
        try{
            watchRepository.deleteById(id);
        } catch(EmptyResultDataAccessException e){
            e.printStackTrace();
        }
    }
}
