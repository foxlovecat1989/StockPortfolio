package com.moresby.ed.stockportfolio.watch;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface WatchlistRepository extends CrudRepository<Watchlist, Long> {

    @Query("SELECT w FROM Watchlist w WHERE w.user.id = ?1")
    List<Watchlist> findAllByUserIdEqual(Long userId);
}
