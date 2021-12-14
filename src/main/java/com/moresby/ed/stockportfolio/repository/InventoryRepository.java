package com.moresby.ed.stockportfolio.repository;

import com.moresby.ed.stockportfolio.domain.Inventory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends CrudRepository<Inventory, Long> {

    @Query(value ="SELECT i FROM Inventory i WHERE i.user.id = ?1")
    List<Inventory> findAllByUserId(Long userId);

    @Query(value = "SELECT i FROM Inventory i WHERE i.user.id = ?1 AND i.tStock.id = ?2")
    Optional<Inventory> findOneByUserIdAndTStockId(Long userId, Long tStcokId);
}
