package com.moresby.ed.stockportfolio.service;

import com.moresby.ed.stockportfolio.domain.Account;
import com.moresby.ed.stockportfolio.exception.InsufficientAmount;
import com.moresby.ed.stockportfolio.domain.User;

public interface AccountService {
    Account withdrawal(User user, Long amount) throws IllegalArgumentException, InsufficientAmount;
    Account deposit(User user, Long amount);
    Account findExistingAccountByUserId(Long userId);
}
