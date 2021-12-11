package com.moresby.ed.stockportfolio.account;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping(path = "/{userId}")
    public Account findAccountByUserId(@PathVariable("userId") Long userId){
        return accountService.findExistingAccountByUserId(userId);
    }
}
