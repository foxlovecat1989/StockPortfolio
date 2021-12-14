package com.moresby.ed.stockportfolio.resource;

import com.moresby.ed.stockportfolio.service.ConfirmEmailTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/v1/confirmToken")
public class ConfirmEmailTokenController {

    private final ConfirmEmailTokenService confirmEmailTokenService;

    @GetMapping
    public String confirmToken(@RequestParam("token") String token){

        return  confirmEmailTokenService.confirmToken(token);
    }
}
