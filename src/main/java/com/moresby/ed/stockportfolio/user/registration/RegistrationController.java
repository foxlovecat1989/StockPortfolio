package com.moresby.ed.stockportfolio.user.registration;

import com.moresby.ed.stockportfolio.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/registration")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping
    public User registration(@RequestBody RegistrationRequest registrationRequest){
        return registrationService.registration(registrationRequest);
    }
}
