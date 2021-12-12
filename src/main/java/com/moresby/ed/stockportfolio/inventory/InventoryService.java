package com.moresby.ed.stockportfolio.inventory;

import com.moresby.ed.stockportfolio.exception.InsufficientAmount;
import com.moresby.ed.stockportfolio.trade.model.pojo.TradePOJO;
import java.util.List;
import java.util.Optional;

public interface InventoryService {
    Inventory add(Inventory inventory);
    List<Inventory> findAllByUserId(Long userId);
    Inventory update(Inventory inventory);
    Inventory updateInventory(TradePOJO tradePOJO) throws InsufficientAmount;
    Inventory findExistingInventoryByUseIdAndStockId(Long userId, Long stockId);
    Optional<Inventory> findByInventoryId(Long id);
    double calculateAvgPriceInInventory(TradePOJO tradePOJO);
    void remove(Inventory inventory);
    Optional<Inventory> findInventoryByUseIdAndStockId(Long userId, Long stockId);
}
