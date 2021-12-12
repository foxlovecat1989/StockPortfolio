package com.moresby.ed.stockportfolio.user.registration.token;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/v1/confirmToken")
public class ConfirmEmailTokenController {

    private final ConfirmEmailTokenService confirmEmailTokenService;

    @GetMapping(path = "/{token}")
    public String confirmToken(@PathVariable("token") String token){

        return  confirmEmailTokenService.confirmToken(token);
    }
}
