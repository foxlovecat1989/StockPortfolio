package com.moresby.ed.stockportfolio.repository;

import com.moresby.ed.stockportfolio.domain.Watchlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WatchlistRepository extends JpaRepository<Watchlist, Long> {

    @Query("SELECT w FROM Watchlist w WHERE w.user.id = ?1")
    List<Watchlist> findAllByUserId(Long userId);
}
