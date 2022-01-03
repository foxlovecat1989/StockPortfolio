package com.moresby.ed.stockportfolio.resource;

import com.moresby.ed.stockportfolio.exception.domain.trade.BankAccountNotFoundException;
import com.moresby.ed.stockportfolio.exception.domain.user.UserNotFoundException;
import com.moresby.ed.stockportfolio.exception.handler.TradeExceptionHandling;
import com.moresby.ed.stockportfolio.service.AccountService;
import com.moresby.ed.stockportfolio.domain.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/api/v1/account")
@RequiredArgsConstructor
public class AccountController extends TradeExceptionHandling {

    private final AccountService accountService;

    @GetMapping(path = "/{userNumber}", produces = APPLICATION_JSON_VALUE)
    @PreAuthorize(value = "hasAnyAuthority('account:read')")
    public ResponseEntity<Account> findAccountByUserNumber(@PathVariable("userNumber") String userNumber)
            throws BankAccountNotFoundException, UserNotFoundException {
        var account =  accountService.findExistingAccountByUserNumber(userNumber);

        return new ResponseEntity<>(account, HttpStatus.OK);
    }
}
