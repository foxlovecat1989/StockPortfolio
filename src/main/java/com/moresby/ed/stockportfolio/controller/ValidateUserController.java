package com.moresby.ed.stockportfolio.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/basicAuth")
public class ValidateUserController {

    @RequestMapping(path = "/validate")
    public String userIsValid(){
        return "{\"result\" : \"ok\"}";
    }
}
