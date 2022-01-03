package com.moresby.ed.stockportfolio.service.impl;

import com.moresby.ed.stockportfolio.domain.Inventory;
import com.moresby.ed.stockportfolio.exception.domain.trade.InSufficientAmountInInventoryException;
import com.moresby.ed.stockportfolio.exception.domain.trade.InputNumberNegativeException;
import com.moresby.ed.stockportfolio.exception.domain.trade.InventoryNotFoundException;
import com.moresby.ed.stockportfolio.exception.domain.user.UserNotFoundException;
import com.moresby.ed.stockportfolio.repository.InventoryRepository;
import com.moresby.ed.stockportfolio.service.InventoryService;
import com.moresby.ed.stockportfolio.enumeration.TradeType;
import com.moresby.ed.stockportfolio.domain.TradePOJO;
import com.moresby.ed.stockportfolio.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.moresby.ed.stockportfolio.constant.TradeImplConstant.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final UserService userService;

    @Override
    public Inventory add(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }


    @Override
    public List<Inventory> findAllByUserNumber(String userNumber) throws UserNotFoundException {
        var user = userService.findExistingUserByUserNumber(userNumber);

        return inventoryRepository.findAllByUserNumber(user.getUserNumber());
    }

    @Override
    public double calculateAvgPriceInInventory(TradePOJO tradePOJO, Optional<Inventory> optInventory) {
        var stock = tradePOJO.getTStock();
        var amount = tradePOJO.getAmount();

        return (optInventory.isEmpty() ?
                        stock.getPrice().doubleValue() :
                        (optInventory.get().getAvgPrice().doubleValue()
                                * optInventory.get().getAmount() + stock.getPrice().doubleValue()
                                * amount)
                                / (optInventory.get().getAmount() + amount));
    }

    @Override
    public void updateInventory(TradePOJO tradePOJO)
            throws InSufficientAmountInInventoryException, InputNumberNegativeException, InventoryNotFoundException {

        if(tradePOJO.getAmount() < 0)
            throw new InputNumberNegativeException(String.format(INPUT_AMOUNT_CANNOT_BE_NEGATIVE, tradePOJO.getAmount()));

        Optional<Inventory> optInventory =
                inventoryRepository.findInventoryByUserNumberAndStockId(
                        tradePOJO.getUser().getUserNumber(),
                        tradePOJO.getTStock().getId());
        Inventory inventory;
        if(tradePOJO.getTradeType() == TradeType.BUY){      // under buy mode
            inventory = optInventory.orElseGet(Inventory::new);
            inventory.setUser(tradePOJO.getUser());
            inventory.setTStock(tradePOJO.getTStock());
            inventory.setAmount(
                    tradePOJO.getAmount() + (optInventory.isPresent() ? optInventory.get().getAmount() : 0)
            );
            double avgPrice = calculateAvgPriceInInventory(tradePOJO, optInventory);
            inventory.setAvgPrice(BigDecimal.valueOf(avgPrice));
        }else {                                             // under sell mode
            inventory = optInventory.orElseThrow(
                    () -> {
                        var errorMsg =
                                String.format(ZERO_AMOUNT_OF_STOCK_IN_INVENTORY, tradePOJO.getTStock().getSymbol());
                        log.warn(errorMsg);
                        return new InventoryNotFoundException(errorMsg);
                    }
            );

            inventory.setUser(tradePOJO.getUser());
            inventory.setTStock(tradePOJO.getTStock());

            var isRemainAmountSufficient = inventory.getAmount() - tradePOJO.getAmount() > 0;
           if(!isRemainAmountSufficient){
               var errorMsg = String.format(INSUFFICIENT_AMOUNT_IN_INVENTORY, inventory.getAmount());
               log.warn(errorMsg);
               throw new InSufficientAmountInInventoryException(errorMsg);
           }
            inventory.setAmount(
                    inventory.getAmount() - tradePOJO.getAmount()
            );
            inventory.setAvgPrice(inventory.getAvgPrice());
        }

        inventoryRepository.save(inventory);
    }
}
