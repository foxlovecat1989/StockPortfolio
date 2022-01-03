package com.moresby.ed.stockportfolio.service;

import com.moresby.ed.stockportfolio.domain.Inventory;
import com.moresby.ed.stockportfolio.domain.TradePOJO;
import com.moresby.ed.stockportfolio.exception.domain.trade.InSufficientAmountInInventoryException;
import com.moresby.ed.stockportfolio.exception.domain.trade.InputNumberNegativeException;
import com.moresby.ed.stockportfolio.exception.domain.trade.InventoryNotFoundException;

import java.util.List;
import java.util.Optional;

public interface InventoryService {
    Inventory add(Inventory inventory);
    List<Inventory> findAllByUserNumber(String userNumber);
    Inventory updateInventory(TradePOJO tradePOJO) throws InSufficientAmountInInventoryException, InputNumberNegativeException, InventoryNotFoundException;
    Optional<Inventory> findByInventoryId(Long id);
    double calculateAvgPriceInInventory(TradePOJO tradePOJO);
    Optional<Inventory> findInventoryByUserNumberAndStockId(String userNumber, Long stockId);
    Inventory findExistingInventoryByUserNumberAndStockId(String userNumber, Long stockId)
            throws InventoryNotFoundException;
}
