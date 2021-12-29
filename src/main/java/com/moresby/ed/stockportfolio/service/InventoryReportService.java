package com.moresby.ed.stockportfolio.service;

import com.moresby.ed.stockportfolio.domain.InventoryReport;
import com.moresby.ed.stockportfolio.exception.domain.user.UserNotFoundException;

import java.util.List;

public interface InventoryReportService {
    List<InventoryReport> getReportsByUserNumber(String userNumber) throws UserNotFoundException;
}
