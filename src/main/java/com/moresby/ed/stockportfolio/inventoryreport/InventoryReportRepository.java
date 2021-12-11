package com.moresby.ed.stockportfolio.inventoryreport;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface InventoryReportRepository extends CrudRepository<InventoryReport, Long> {
    @Query(value = "SELECT ir FROM InventReport ir WHERE ir.userId = ?1")
    List<InventoryReport> findAllByUserId(Long userId);
}
