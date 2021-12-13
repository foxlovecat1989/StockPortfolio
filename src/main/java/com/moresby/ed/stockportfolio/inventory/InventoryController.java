package com.moresby.ed.stockportfolio.inventory;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/inventories")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping(value = "/findAll/{userId}")
    public List<Inventory> findAllByUserId(@PathVariable("userId") Long userId){

        return inventoryService.findAllByUserId(userId);
    }
}
