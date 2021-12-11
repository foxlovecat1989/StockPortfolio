package com.moresby.ed.stockportfolio.inventory;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService{

    private final InventoryRepository inventoryRepository;

    @Override
    public Optional<Inventory> findOneByUserIdAndTStockId(Long userId, Long tStcokId) {
        return inventoryRepository.findOneByUserIdAndTStockId(userId, tStcokId);
    }

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
}
