package com.moresby.ed.stockportfolio.service;

import com.moresby.ed.stockportfolio.domain.InventoryReport;

import java.util.List;

public interface InventoryReportService {
    List<InventoryReport> getReportsByUserId(Long userId);
}
