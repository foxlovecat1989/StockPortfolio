package com.moresby.ed.stockportfolio.service.impl;

import com.moresby.ed.stockportfolio.domain.Account;
import com.moresby.ed.stockportfolio.domain.RegistrationRequest;
import com.moresby.ed.stockportfolio.service.RegistrationService;
import com.moresby.ed.stockportfolio.domain.User;
import com.moresby.ed.stockportfolio.enumeration.UserRole;
import com.moresby.ed.stockportfolio.service.UserService;
import com.moresby.ed.stockportfolio.service.ConfirmEmailTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegistrationServiceImpl implements RegistrationService {

    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ConfirmEmailTokenService confirmEmailTokenService;

    @Override
    @Transactional
    public User registration(RegistrationRequest registrationRequest) {
        validateEmail(registrationRequest);

        Account account =
                Account.builder()
                        .balance(BigDecimal.ZERO)
                        .build();
        User user =
                User.builder()
                        .username(registrationRequest.getUsername())
                        .password(passwordEncoder.encode(registrationRequest.getPassword()))
                        .email(registrationRequest.getEmail())
                        .userRole(UserRole.USER)
                        .isEnabled(Boolean.FALSE)
                        .isAccountNonLocked(Boolean.TRUE)
                        .account(account)
                        .build();

        account.setUser(user);
        var newUser = userService.createUser(user);
        confirmEmailTokenService.createToken(user);

        return newUser;
    }

    private void validateEmail(RegistrationRequest registrationRequest) {
        // TODO: Validate User email - if(registrationRequest.getEmail())
        boolean isEmailBeTaken = userService.isEmailTaken(registrationRequest.getEmail());
        if (isEmailBeTaken){
            log.warn(String.format("Email: %s is already be taken", registrationRequest.getEmail()));
            throw new IllegalStateException(String.format("Email: %s is already be taken", registrationRequest.getEmail()));
        }
    }
}
