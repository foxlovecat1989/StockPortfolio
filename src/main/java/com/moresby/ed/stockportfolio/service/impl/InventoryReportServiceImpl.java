package com.moresby.ed.stockportfolio.service.impl;

import com.moresby.ed.stockportfolio.domain.InventoryReport;
import com.moresby.ed.stockportfolio.exception.domain.user.UserNotFoundException;
import com.moresby.ed.stockportfolio.repository.InventoryReportRepository;
import com.moresby.ed.stockportfolio.service.InventoryReportService;
import com.moresby.ed.stockportfolio.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryReportServiceImpl implements InventoryReportService  {

    private final InventoryReportRepository inventoryReportRepository;
    private final UserService userService;

    @Override
    public List<InventoryReport> getReportsByUserNumber(String userNumber) throws UserNotFoundException {
        var user = userService.findExistingUserByUserNumber(userNumber);

        return inventoryReportRepository.findAllByUserId(user.getId());
    }
}
