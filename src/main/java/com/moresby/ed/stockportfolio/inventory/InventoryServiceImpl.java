package com.moresby.ed.stockportfolio.inventory;

import com.moresby.ed.stockportfolio.exception.InsufficientAmount;
import com.moresby.ed.stockportfolio.trade.model.enumeration.TradeType;
import com.moresby.ed.stockportfolio.trade.model.pojo.TradePOJO;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService{

    private final InventoryRepository inventoryRepository;

    @Override
    public Inventory add(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }

    @Override
    public Optional<Inventory> findByInventoryId(Long id) {
        return inventoryRepository.findById(id);
    }

    @Override
    public List<Inventory> findAllByUserId(Long userId) {
        return inventoryRepository.findAllByUserId(userId);
    }

    @Override
    public Inventory update(Inventory inventory) {
        Inventory originInventory = inventoryRepository.findById(inventory.getId())
                .orElseThrow(
                        ()-> new NoSuchElementException(
                                String.format("Inventory ID: %s Not Found", inventory.getId()))
                );

//        originInventory.setTStock(
//                inventory.getTStock() != null ? inventory.getTStock() : originInventory.getTStock()
//        );
//        originInventory.setUser(
//                inventory.getUser() != null ? inventory.getUser() : originInventory.getUser()
//        );
//        originInventory.setAvgPrice(inventory.getAvgPrice());
//        originInventory.setTotalCost(inventory.getTotalCost());
//        originInventory.setAmount(inventory.getAmount());
//        originInventory.setLastUpdate( new java.sql.Date(new Date().getTime()));


        return inventoryRepository.save(originInventory);
    }

    @Override
    public void remove(Inventory inventory) {
        try{
            inventoryRepository.deleteById(inventory.getId());
        } catch(EmptyResultDataAccessException e){
            e.printStackTrace();
        }
    }

    @Override
    public double calculateAvgPriceInInventory(TradePOJO tradePOJO) {
        var stock = tradePOJO.getTStock();
        var amount = tradePOJO.getAmount();
        Optional<Inventory> optInventory = findInventoryByUseIdAndStockId(tradePOJO.getUser().getId(), tradePOJO.getTStock().getId());
        double avgPrice =
                (optInventory.isEmpty() ?
                        stock.getPrice().doubleValue() :
                        (optInventory.get().getAvgPrice().doubleValue()
                                * optInventory.get().getAmount() + stock.getPrice().doubleValue()
                                * amount)
                                / (optInventory.get().getAmount() + amount));

        return avgPrice;
    }

    @Override
    public Inventory updateInventory(TradePOJO tradePOJO) throws InsufficientAmount,IllegalArgumentException {
        Optional<Inventory> optInventory =
                inventoryRepository.findOneByUserIdAndTStockId(tradePOJO.getUser().getId(), tradePOJO.getTStock().getId());
        Inventory inventory =
                optInventory.isPresent() ? optInventory.get() : new Inventory();
        inventory.setUser(tradePOJO.getUser());
        inventory.setTStock(tradePOJO.getTStock());

        if(tradePOJO.getTradeType() == TradeType.BUY){      // under buy mode
            inventory.setAmount(
                    tradePOJO.getAmount() + (optInventory.isPresent() ? optInventory.get().getAmount() : 0)
            );
            double avgPrice = calculateAvgPriceInInventory(tradePOJO);
            inventory.setAvgPrice(BigDecimal.valueOf(avgPrice));
        }else {                                             // undersell mode
           if(inventory.getAmount() - tradePOJO.getAmount() < 0)
               throw new InsufficientAmount("Insufficient amount in your inventory");
            inventory.setAmount(
                    inventory.getAmount() - tradePOJO.getAmount()
            );
            inventory.setAvgPrice(inventory.getAvgPrice());
        }

        inventoryRepository.save(inventory);

        return inventory;
    }

    @Override
    public Inventory findExistingInventoryByUseIdAndStockId(Long userId, Long stockId){
      return inventoryRepository.findOneByUserIdAndTStockId(userId, stockId).orElseThrow(
              () -> new NoSuchElementException(
                      String.format("Inventory with UserId: %s  & Stock Id: %s Not Found", userId, stockId))
      );
    }

    @Override
    public Optional<Inventory> findInventoryByUseIdAndStockId(Long userId, Long stockId){
        return inventoryRepository.findOneByUserIdAndTStockId(userId, stockId);
    }
}
