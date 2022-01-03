package com.moresby.ed.stockportfolio.service.impl;

import com.moresby.ed.stockportfolio.domain.Account;
import com.moresby.ed.stockportfolio.domain.TradePOJO;
import com.moresby.ed.stockportfolio.enumeration.TradeType;
import com.moresby.ed.stockportfolio.exception.domain.trade.BankAccountNotFoundException;
import com.moresby.ed.stockportfolio.exception.domain.trade.InSufficientBalanceException;
import com.moresby.ed.stockportfolio.exception.domain.trade.InputNumberNegativeException;
import com.moresby.ed.stockportfolio.exception.domain.user.UserNotFoundException;
import com.moresby.ed.stockportfolio.repository.AccountRepository;
import com.moresby.ed.stockportfolio.service.AccountService;
import com.moresby.ed.stockportfolio.service.UserService;
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
    private final UserService userService;

    @Override
    public void withdrawal(Account account, Long amount)
            throws
            InSufficientBalanceException,
            InputNumberNegativeException{
        var isAmountLessThanZero = amount < 0;
        if(isAmountLessThanZero)
            throw new InputNumberNegativeException(String.format(INPUT_AMOUNT_CANNOT_BE_NEGATIVE, amount));

        var isSufficientBalance = account.getBalance().doubleValue() - amount > 0;
        if(!isSufficientBalance){
            var errorMsg = String.format(INSUFFICIENT_BALANCE, account.getBalance());
            log.error(errorMsg);
            throw new InSufficientBalanceException(errorMsg);
        }

        account.setBalance(BigDecimal.valueOf(account.getBalance().doubleValue() - amount));


        accountRepository.save(account);
    }

    @Override
    public void deposit(Account account, Long amount) {
        var newBalance = BigDecimal.valueOf(account.getBalance().doubleValue() + amount);
        account.setBalance(newBalance);

        accountRepository.save(account);
    }

    @Override
    public Account findExistingAccountByUserNumber(String userNumber)
            throws BankAccountNotFoundException, UserNotFoundException {
        var user = userService.findExistingUserByUserNumber(userNumber);

        return  accountRepository.findById(user.getAccount().getId())
                    .orElseThrow(
                        () -> {
                            var errorMsg = String.format(NO_ACCOUNT_FOUND_BY_USER_NUMBER, userNumber);
                            log.error(errorMsg);
                            return new BankAccountNotFoundException(errorMsg);
                        }
                    );
    }

    @Override
    public void executeTrade(TradePOJO tradePOJO, Long amount)
            throws
            BankAccountNotFoundException,
            InSufficientBalanceException,
            InputNumberNegativeException,
            UserNotFoundException {
        Account account = findExistingAccountByUserNumber(tradePOJO.getUser().getUserNumber());
        if (tradePOJO.getTradeType() == TradeType.BUY)
            withdrawal(account, amount);
        else
            deposit(account, amount);
    }
}
