package com.moresby.ed.stockportfolio.controller;

import com.moresby.ed.stockportfolio.service.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/basicAuth")
@RequiredArgsConstructor
public class ValidateUserController {

    private final JWTService jwtService;
    private final int INDEX_OF_EXTRACT_ROLE_BEGIN = 5;

    @RequestMapping(path = "validate")
    public Map<String, String> userIsValid(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();
        String name = currentUser.getUsername();
        String role = currentUser.getAuthorities().toArray()[0].toString().substring(INDEX_OF_EXTRACT_ROLE_BEGIN);
        String token = jwtService.generateToken(name, role);
        Map<String, String> result = new HashMap<>();
        result.put("result", token);

        return result;
    }
}

