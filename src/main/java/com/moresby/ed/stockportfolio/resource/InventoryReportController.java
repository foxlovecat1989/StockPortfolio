package com.moresby.ed.stockportfolio.resource;

import com.moresby.ed.stockportfolio.domain.InventoryReport;
import com.moresby.ed.stockportfolio.exception.domain.user.UserNotFoundException;
import com.moresby.ed.stockportfolio.exception.handler.InventoryExceptionHandling;
import com.moresby.ed.stockportfolio.service.InventoryReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/api/v1/report")
@RequiredArgsConstructor
public class InventoryReportController extends InventoryExceptionHandling {

    private final InventoryReportService inventoryReportService;

    @GetMapping(path = "/{userNumber}", produces = APPLICATION_JSON_VALUE)
    @PreAuthorize(value = "hasAnyAuthority('inventory:read')")
    public ResponseEntity<List<InventoryReport>> report(@PathVariable("userNumber") String userNumber)
            throws UserNotFoundException {
        var inventoryReports =  inventoryReportService.getReportsByUserNumber(userNumber);

        return new ResponseEntity<>(inventoryReports, HttpStatus.OK);
    }
}
