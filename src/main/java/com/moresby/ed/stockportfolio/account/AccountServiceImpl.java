package com.moresby.ed.stockportfolio.account;

import com.moresby.ed.stockportfolio.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService{

    private final AccountRepository accountRepository;

    @Override
    public Account withdrawal(User user, Long amount) {
        if(amount < 0)
            throw new IllegalArgumentException("amount cannot be negative");

        Account account = findExistingAccountByUserId(user.getId());
        if(account.getBalance().doubleValue() - amount < 0) {
            return null; // TODO: EXCEPTION HANDLE
        }
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
