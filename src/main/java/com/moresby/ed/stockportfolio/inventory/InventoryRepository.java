package com.moresby.ed.stockportfolio.inventory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    @Query(value = "SELECT i FROM Inventory i WHERE i.userId = ?1")
    List<Inventory> findByUserId(Long userId);
}
