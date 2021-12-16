package com.moresby.ed.stockportfolio.service.impl;

import com.moresby.ed.stockportfolio.domain.RegistrationRequest;
import com.moresby.ed.stockportfolio.exception.domain.EmailExistException;
import com.moresby.ed.stockportfolio.exception.domain.UsernameExistException;
import com.moresby.ed.stockportfolio.service.RegistrationService;
import com.moresby.ed.stockportfolio.domain.User;
import com.moresby.ed.stockportfolio.service.UserService;
import com.moresby.ed.stockportfolio.service.ConfirmEmailTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegistrationServiceImpl implements RegistrationService {

    private final UserService userService;
    private final ConfirmEmailTokenService confirmEmailTokenService;

    @Override
    @Transactional
    public User registration(RegistrationRequest registrationRequest)
            throws UsernameExistException, EmailExistException {
        var user = userService.register(registrationRequest);
        confirmEmailTokenService.createToken(user);

        return user;
    }

}
