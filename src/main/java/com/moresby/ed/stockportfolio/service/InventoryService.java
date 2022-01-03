package com.moresby.ed.stockportfolio.service;

import com.moresby.ed.stockportfolio.domain.Inventory;
import com.moresby.ed.stockportfolio.domain.TradePOJO;
import com.moresby.ed.stockportfolio.exception.domain.trade.InSufficientAmountInInventoryException;
import com.moresby.ed.stockportfolio.exception.domain.trade.InputNumberNegativeException;
import com.moresby.ed.stockportfolio.exception.domain.trade.InventoryNotFoundException;
import com.moresby.ed.stockportfolio.exception.domain.user.UserNotFoundException;

import java.util.List;
import java.util.Optional;

public interface InventoryService {
    Inventory add(Inventory inventory);
    List<Inventory> findAllByUserNumber(String userNumber) throws UserNotFoundException;
    void updateInventory(TradePOJO tradePOJO)
            throws InSufficientAmountInInventoryException, InputNumberNegativeException, InventoryNotFoundException;
    double calculateAvgPriceInInventory(TradePOJO tradePOJO, Optional<Inventory> optInventory);
}
