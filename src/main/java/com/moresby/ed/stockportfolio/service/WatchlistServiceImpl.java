package com.moresby.ed.stockportfolio.service;

import com.moresby.ed.stockportfolio.model.WatchlistId;
import com.moresby.ed.stockportfolio.model.entities.Watchlist;
import com.moresby.ed.stockportfolio.repo.WatchlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WatchlistServiceImpl implements WatchlistService {

    private final WatchlistRepository watchlistRepository;


    @Override
    public Optional<Watchlist> findOneById(WatchlistId watchlistId) {
        return watchlistRepository.findById(watchlistId);
    }

    @Override
    public Iterable<Watchlist> findAllByUserId(Long investorId) {
        return null;
    }

    @Override
    public Iterable<Watchlist> findAll() {
        return watchlistRepository.findAll();
    }

    @Override
    public Watchlist createOne(Watchlist watchlist) {
        return watchlistRepository.save(watchlist);
    }

    @Override
    public Watchlist refreshLastUpdateDate(Watchlist watchlist) {
        Watchlist originOne =
                watchlistRepository
                    .findById(watchlist.getWatchlistId())
                        .orElseThrow(
                                ()-> new IllegalStateException(
                                        String.format("ID: %s is Not Found", watchlist.getWatchlistId())
                                )
                        );
        originOne.setLastUpdateDate(LocalDateTime.now());

        return watchlistRepository.save(watchlist);
    }

    @Override
    public void deleteById(WatchlistId watchlistId) {
        try{
            watchlistRepository.deleteById(watchlistId);
        } catch(EmptyResultDataAccessException e){
            e.printStackTrace();
        }
    }
}
