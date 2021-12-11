package com.moresby.ed.stockportfolio.inventoryreport;

import java.util.List;

public interface InventoryReportService {
    List<InventoryReport> getReportByUserId(Long userId);
}
