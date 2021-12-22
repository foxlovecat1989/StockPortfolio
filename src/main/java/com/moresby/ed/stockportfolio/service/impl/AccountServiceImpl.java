package com.moresby.ed.stockportfolio.service.impl;

import com.moresby.ed.stockportfolio.domain.Account;
import com.moresby.ed.stockportfolio.exception.domain.trade.BankAccountNotFoundException;
import com.moresby.ed.stockportfolio.exception.domain.trade.InSufficientBalanceException;
import com.moresby.ed.stockportfolio.exception.domain.trade.InputNumberNegativeException;
import com.moresby.ed.stockportfolio.repository.AccountRepository;
import com.moresby.ed.stockportfolio.service.AccountService;
import com.moresby.ed.stockportfolio.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

import static com.moresby.ed.stockportfolio.constant.TradeImplConstant.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public Account withdrawal(User user, Long amount)
            throws
            IllegalArgumentException,
            InSufficientBalanceException,
            InputNumberNegativeException,
            BankAccountNotFoundException {

        if(amount < 0)
            throw new InputNumberNegativeException(INPUT_AMOUNT_CANNOT_BE_NEGATIVE);

        Account account = findExistingAccountByUserNumber(user.getUserNumber());
        if(account.getBalance().doubleValue() - amount < 0){
            var errorMsg = String.format(INSUFFICIENT_BALANCE, account.getBalance());
            log.error(errorMsg);
            throw new InSufficientBalanceException(errorMsg);
        }

        account.setBalance(BigDecimal.valueOf(account.getBalance().doubleValue() - amount));


        return accountRepository.save(account);
    }

    @Override
    public Account deposit(User user, Long amount) throws BankAccountNotFoundException {
        Account account = findExistingAccountByUserNumber(user.getUserNumber());
        var newBalance = BigDecimal.valueOf(account.getBalance().doubleValue() + amount);
        account.setBalance(newBalance);

        return accountRepository.save(account);
    }

    @Override
    public Account findExistingAccountByUserNumber(String userNumber) throws BankAccountNotFoundException {
        return accountRepository.findOneByUserNumber(userNumber)
                .orElseThrow(
                    () -> {
                        var errorMsg = String.format(NO_ACCOUNT_FOUND_BY_USER_NUMBER, userNumber);
                        log.error(errorMsg);
                        return new BankAccountNotFoundException(errorMsg);
                    }
                );
    }
}
