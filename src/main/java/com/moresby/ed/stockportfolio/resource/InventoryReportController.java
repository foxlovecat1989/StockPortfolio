package com.moresby.ed.stockportfolio.resource;

import com.moresby.ed.stockportfolio.domain.InventoryReport;
import com.moresby.ed.stockportfolio.service.InventoryReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/report")
@RequiredArgsConstructor
public class InventoryReportController {

    private final InventoryReportService inventoryReportService;

    @GetMapping(path = "/{userId}")
    public ResponseEntity<List<InventoryReport>> report(@PathVariable("userId") Long userId){
        var inventoryReports =  inventoryReportService.getReportsByUserId(userId);

        return new ResponseEntity<>(inventoryReports, HttpStatus.OK);
    }
}
