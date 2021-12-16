package com.moresby.ed.stockportfolio.service.impl;

import com.moresby.ed.stockportfolio.domain.Account;
import com.moresby.ed.stockportfolio.domain.RegistrationRequest;
import com.moresby.ed.stockportfolio.exception.domain.EmailExistException;
import com.moresby.ed.stockportfolio.exception.domain.UsernameExistException;
import com.moresby.ed.stockportfolio.service.RegistrationService;
import com.moresby.ed.stockportfolio.domain.User;
import com.moresby.ed.stockportfolio.enumeration.UserRole;
import com.moresby.ed.stockportfolio.service.UserService;
import com.moresby.ed.stockportfolio.service.ConfirmEmailTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;

import static com.moresby.ed.stockportfolio.constant.FileConstant.DEFAULT_USER_IMAGE_PATH;
import static com.moresby.ed.stockportfolio.constant.UserImplConstant.EMAIL_ALREADY_EXISTS;
import static com.moresby.ed.stockportfolio.constant.UserImplConstant.USERNAME_ALREADY_EXISTS;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegistrationServiceImpl implements RegistrationService {

    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ConfirmEmailTokenService confirmEmailTokenService;

    @Override
    @Transactional
    public User registration(RegistrationRequest registrationRequest) throws UsernameExistException, EmailExistException {
        validateNewUsernameAndEmail(registrationRequest);
        Account account =
                Account.builder()
                        .balance(BigDecimal.ZERO)
                        .build();
        User user =
                User.builder()
                        .user_number(generateUserNumber())
                        .username(registrationRequest.getUsername())
                        .password(passwordEncoder.encode(registrationRequest.getPassword()))
                        .email(registrationRequest.getEmail())
                        .userRole(UserRole.ROLE_USER)
                        .joinDate(new Date())
                        .profileImageUrl(getTemporaryProfileImageUrl(registrationRequest.getUsername()))
                        .isEnabled(Boolean.TRUE)
                        .isAccountNonLocked(Boolean.TRUE)
                        .account(account)
                        .build();

        account.setUser(user);
        // confirmEmailTokenService.createToken(user);

        return userService.createUser(user);
    }


    private void validateNewUsernameAndEmail(RegistrationRequest registrationRequest)
            throws UsernameExistException, EmailExistException{
        boolean isEmailBeTaken = userService.isEmailTaken(registrationRequest.getEmail());
        boolean isUsernameBeTaken = userService.isUsernameTaken(registrationRequest.getUsername());
        if (isUsernameBeTaken)
            throw new UsernameExistException(USERNAME_ALREADY_EXISTS);
        if (isEmailBeTaken)
            throw new EmailExistException(EMAIL_ALREADY_EXISTS);
    }

    private String generateUserNumber() {
        String userNumber;
        boolean isUserNumberTaken;
        do {
            userNumber = RandomStringUtils.randomNumeric(10);
            isUserNumberTaken = userService.isUserNumberTaken(userNumber);
        } while (isUserNumberTaken);

        return userNumber;
    }

    private String getTemporaryProfileImageUrl(String username) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(DEFAULT_USER_IMAGE_PATH + username).toUriString();
    }
}
