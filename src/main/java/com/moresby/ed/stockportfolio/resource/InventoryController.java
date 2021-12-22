package com.moresby.ed.stockportfolio.resource;

import com.moresby.ed.stockportfolio.domain.Inventory;
import com.moresby.ed.stockportfolio.exception.handler.TradeExceptionHandling;
import com.moresby.ed.stockportfolio.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/api/v1/inventorie")
@RequiredArgsConstructor
public class InventoryController extends TradeExceptionHandling {

    private final InventoryService inventoryService;

    @GetMapping(value = "/findAll/{userNumber}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Inventory>> findAllByUserNumber(@PathVariable("userNumber") String userNumber){
        var inventories =  inventoryService.findAllByUserNumber(userNumber);

        return new ResponseEntity<>(inventories, HttpStatus.OK);
    }
}
