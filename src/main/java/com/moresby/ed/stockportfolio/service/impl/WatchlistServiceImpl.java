package com.moresby.ed.stockportfolio.service.impl;

import com.moresby.ed.stockportfolio.domain.Watchlist;
import com.moresby.ed.stockportfolio.repository.WatchlistRepository;
import com.moresby.ed.stockportfolio.service.WatchlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WatchlistServiceImpl implements WatchlistService {

    private final WatchlistRepository watchlistRepository;

    @Override
    public Watchlist createWatch(Watchlist watchlist) {
        return watchlistRepository.save(watchlist);
    }

    @Override
    public Optional<Watchlist> findOneById(Long id) {
        return watchlistRepository.findById(id);
    }

    @Override
    public Iterable<Watchlist> findAllByUserId(Long userId) {
        return watchlistRepository.findAllByUserIdEqual(userId);
    }

    @Override
    public Iterable<Watchlist> findAll() {
        return watchlistRepository.findAll();
    }

    @Override
    public Watchlist updateWatch(Watchlist watchlist) {
        var originWatch = watchlistRepository.findById(watchlist.getId()).orElseThrow(() -> new IllegalStateException("update watch fail exception"));
        originWatch.setName(watchlist.getName() != null ? watchlist.getName() : originWatch.getName());

        return watchlistRepository.save(originWatch);
    }

    @Override
    public void deleteById(Long id) {
        try{
            watchlistRepository.deleteById(id);
        } catch(EmptyResultDataAccessException e){
            e.printStackTrace();
        }
    }
}
