package com.moresby.ed.stockportfolio.resource;

import com.moresby.ed.stockportfolio.domain.User;
import com.moresby.ed.stockportfolio.domain.RegistrationRequest;
import com.moresby.ed.stockportfolio.exception.domain.EmailExistException;
import com.moresby.ed.stockportfolio.exception.domain.UsernameExistException;
import com.moresby.ed.stockportfolio.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/registration")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping
    public ResponseEntity<User> registration(@RequestBody RegistrationRequest registrationRequest)
            throws EmailExistException, UsernameExistException {
        var user = registrationService.registration(registrationRequest);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
