package com.moresby.ed.stockportfolio.service.impl;

import com.moresby.ed.stockportfolio.domain.InventoryReport;
import com.moresby.ed.stockportfolio.repository.InventoryReportRepository;
import com.moresby.ed.stockportfolio.service.InventoryReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryReportServiceImpl implements InventoryReportService {
    private final InventoryReportRepository inventoryReportRepository;

    @Override
    public List<InventoryReport> getReportsByUserId(Long userId) {
        return inventoryReportRepository.findAllByUserId(userId);
    }
}
