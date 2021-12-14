package com.moresby.ed.stockportfolio.service.impl;

import com.moresby.ed.stockportfolio.domain.Account;
import com.moresby.ed.stockportfolio.exception.InsufficientAmount;
import com.moresby.ed.stockportfolio.repository.AccountRepository;
import com.moresby.ed.stockportfolio.service.AccountService;
import com.moresby.ed.stockportfolio.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public Account withdrawal(User user, Long amount) throws IllegalArgumentException, InsufficientAmount {
        if(amount < 0)
            throw new IllegalArgumentException("Amount cannot be negative");

        Account account = findExistingAccountByUserId(user.getId());
        if(account.getBalance().doubleValue() - amount < 0)
            throw new InsufficientAmount("Insufficient Balance in your account");

        account.setBalance(BigDecimal.valueOf(account.getBalance().doubleValue() - amount));

        return accountRepository.save(account);
    }

    @Override
    public Account deposit(User user, Long amount) {
        Account account = findExistingAccountByUserId(user.getId());
        var newBalance = BigDecimal.valueOf(account.getBalance().doubleValue() + amount);
        account.setBalance(newBalance);

        return accountRepository.save(account);
    }

    @Override
    public Account findExistingAccountByUserId(Long userId){
        return accountRepository.findOneByUserId(userId)
                .orElseThrow(
                    () -> new NoSuchElementException(String.format("User Id: %s's Account Not Found", userId))
                );
    }
}
