package com.moresby.ed.stockportfolio.inventory;

import java.util.List;
import java.util.Optional;

public interface InventoryService {
    Optional<Inventory> findOneByUserIdAndTStockId(Long userId, Long tStcokId);

    Inventory add(Inventory inventory);
    Optional<Inventory> findByInventoryId(Long id);
    List<Inventory> findAllByUserId(Long userId);
    Inventory update(Inventory inventory);
    void remove(Inventory inventory);
}
