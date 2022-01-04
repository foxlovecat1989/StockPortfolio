package com.moresby.ed.stockportfolio.service;

import com.moresby.ed.stockportfolio.domain.Account;
import com.moresby.ed.stockportfolio.domain.TradePOJO;
import com.moresby.ed.stockportfolio.exception.domain.trade.BankAccountNotFoundException;
import com.moresby.ed.stockportfolio.exception.domain.trade.InSufficientBalanceException;
import com.moresby.ed.stockportfolio.exception.domain.trade.InputNumberNegativeException;
import com.moresby.ed.stockportfolio.exception.domain.user.UserNotFoundException;

public interface AccountService {
    void withdrawal(Account account, Long amount)
            throws
            InSufficientBalanceException,
            InputNumberNegativeException;

    void deposit(Account account, Long amount);

    Account findExistingAccountByUserNumber(String userNumber) throws BankAccountNotFoundException, UserNotFoundException;

    void  executeTrade(TradePOJO tradePOJO, Long amount)
            throws
            BankAccountNotFoundException,
            InSufficientBalanceException,
            InputNumberNegativeException,
            UserNotFoundException;
}
