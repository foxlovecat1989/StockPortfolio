package com.moresby.ed.stockportfolio.user.registration;

import com.moresby.ed.stockportfolio.account.Account;
import com.moresby.ed.stockportfolio.email.EmailService;
import com.moresby.ed.stockportfolio.user.User;
import com.moresby.ed.stockportfolio.user.UserRole;
import com.moresby.ed.stockportfolio.user.UserService;
import com.moresby.ed.stockportfolio.user.registration.token.ConfirmEmailTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegistrationServiceImpl implements RegistrationService{

    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ConfirmEmailTokenService confirmEmailTokenService;
    private final EmailService emailService;

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
        emailService.sendConfirmEmail(user);

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
