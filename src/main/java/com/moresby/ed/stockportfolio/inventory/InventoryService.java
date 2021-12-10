package com.moresby.ed.stockportfolio.inventory;

import java.util.List;
import java.util.Optional;

public interface InventoryService {
    Inventory add(Inventory inventory);
    Iterable<Inventory> findAll();
    Optional<Inventory> findByInventoryId(Long id);
    Inventory update(Inventory inventory);
    void remove(Inventory inventory);
}
