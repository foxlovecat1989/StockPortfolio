package com.moresby.ed.stockportfolio.service;

import com.moresby.ed.stockportfolio.domain.Account;
import com.moresby.ed.stockportfolio.domain.User;
import com.moresby.ed.stockportfolio.exception.domain.trade.BankAccountNotFoundException;
import com.moresby.ed.stockportfolio.exception.domain.trade.InSufficientBalanceException;
import com.moresby.ed.stockportfolio.exception.domain.trade.InputNumberNegativeException;

public interface AccountService {
    Account withdrawal(User user, Long amount)
            throws
            IllegalArgumentException,
            InSufficientBalanceException,
            BankAccountNotFoundException,
            InputNumberNegativeException;

    Account deposit(User user, Long amount) throws BankAccountNotFoundException;
    Account findExistingAccountByUserNumber(String userNumber) throws BankAccountNotFoundException;
}
