package com.moresby.ed.stockportfolio.repository;

import com.moresby.ed.stockportfolio.domain.Inventory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends CrudRepository<Inventory, Long> {

    @Query(value ="SELECT i FROM Inventory i WHERE i.user.userNumber = ?1")
    List<Inventory> findAllByUserNumber(String userNumber);

    @Query(value = "SELECT i FROM Inventory i WHERE i.user.userNumber = ?1 AND i.tStock.id = ?2")
    Optional<Inventory> findInventoryByUserNumberAndStockId(String userNumber, Long stockId);
}
