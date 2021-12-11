package com.moresby.ed.stockportfolio.inventoryreport;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryReportServiceImpl implements InventoryReportService{
    private final InventoryReportRepository inventoryReportRepository;

    @Override
    public List<InventoryReport> getReportByUserId(Long userId) {
        return inventoryReportRepository.findAllByUserId(userId);
    }
}
