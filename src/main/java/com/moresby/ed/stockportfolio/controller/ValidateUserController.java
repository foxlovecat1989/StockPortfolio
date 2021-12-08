package com.moresby.ed.stockportfolio.controller;

import com.moresby.ed.stockportfolio.service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/basicAuth")
public class ValidateUserController {

    private final JWTService jwtService;
    private final static int INDEX_OF_EXTRACT_ROLE_BEGIN = 5;
    private final static int COOKIE_MAX_AGE_IN_SECONDS = 1800;

    @Autowired
    public ValidateUserController(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    @RequestMapping(path = "/validate")
    public Map<String, String> userIsValid(HttpServletResponse response){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();
        String name = currentUser.getUsername();
        String role = currentUser.getAuthorities().toArray()[0].toString().substring(INDEX_OF_EXTRACT_ROLE_BEGIN);
        String token = jwtService.generateToken(name, role);
        Map<String, String> result = new HashMap<>();
        result.put("result", "ok");

        Cookie cookie = new Cookie("token", token);
        cookie.setPath("/api");
        cookie.setHttpOnly(true);
        // TODO: when production should set cookie.setSecure(true);
        cookie.setMaxAge(COOKIE_MAX_AGE_IN_SECONDS);
        response.addCookie(cookie);

        return result;
    }
}

