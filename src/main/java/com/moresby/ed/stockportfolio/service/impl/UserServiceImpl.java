package com.moresby.ed.stockportfolio.service.impl;

import com.moresby.ed.stockportfolio.domain.Account;
import com.moresby.ed.stockportfolio.domain.RegistrationRequest;
import com.moresby.ed.stockportfolio.domain.User;
import com.moresby.ed.stockportfolio.domain.UserPrincipal;
import com.moresby.ed.stockportfolio.enumeration.UserRole;
import com.moresby.ed.stockportfolio.exception.domain.EmailExistException;
import com.moresby.ed.stockportfolio.exception.domain.UsernameExistException;
import com.moresby.ed.stockportfolio.repository.UserRepository;
import com.moresby.ed.stockportfolio.service.LoginAttemptService;
import com.moresby.ed.stockportfolio.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.util.*;

import static com.moresby.ed.stockportfolio.constant.FileConstant.DEFAULT_USER_IMAGE_PATH;
import static com.moresby.ed.stockportfolio.constant.UserImplConstant.*;
import static com.moresby.ed.stockportfolio.constant.UserImplConstant.EMAIL_ALREADY_EXISTS;
import static com.moresby.ed.stockportfolio.constant.UserImplConstant.USERNAME_ALREADY_EXISTS;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final LoginAttemptService loginAttemptService;

    @Override
    public User findExistingUserById(Long id) {

        return userRepository.findById(id)
                .orElseThrow(
                        () -> {
                            var errorMsg = NO_USER_FOUND_BY_Id + id;
                            log.error(errorMsg);
                            return new UsernameNotFoundException(errorMsg);
                        }
                );
    }

    @Override
    public User findExistingUserByUsername(String username) {

        return userRepository.findUserByUsername(username)
                .orElseThrow(
                        () -> {
                            var errorMsg = NO_USER_FOUND_BY_USERNAME + username;
                            log.error(errorMsg);
                            return new UsernameNotFoundException(errorMsg);
                        }
                );
    }

    @Override
    public User findExistingUserByEmail(String email){
        return userRepository.findUserByEmail(email).orElseThrow(
                () -> {
                    var errorMsg = NO_USER_FOUND_BY_EMAIL + email;
                    log.error(errorMsg);
                    return new UsernameNotFoundException(errorMsg);
                }
        );
    }

    @Override
    public List<User> findAllUsers() {
        List<User> result = new ArrayList<>();
        userRepository.findAll().iterator().forEachRemaining(result::add);

        return result;
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User register(RegistrationRequest registrationRequest) throws EmailExistException, UsernameExistException {
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
                        // TODO: set isEnabled = false when production
                        .isEnabled(Boolean.TRUE)
                        .isAccountNonLocked(Boolean.TRUE)
                        .account(account)
                        .build();

        account.setUser(user);
        return userRepository.save(user);
    }

    // TODO: back to set what properties should be updated
    @Override
    public User updateUser(User user) {
        var existingUser = findExistingUserById(user.getId());
        existingUser.setUsername(
                user.getUsername() != null ? user.getUsername() : existingUser.getUsername()
        );

        return userRepository.save(existingUser);
    }

    @Override
    public void deleteUserById(Long id) {
        try{
            userRepository.deleteById(id);
        } catch(EmptyResultDataAccessException e){
            e.printStackTrace();
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = findExistingUserByUsername(username);
        validateLoginAttempt(user);
        user.setLastLoginDateDisplay(
                user.getLastLoginDate() != null ? user.getLastLoginDate() : new Date()
        );
        user.setLastLoginDate(new Date());
        userRepository.save(user);
        var userPrincipal = new UserPrincipal(user);
        log.info(FOUND_USER_BY_USERNAME + username);

        return userPrincipal;
    }

    @Override
    public boolean isEmailTaken(String email) {

        return userRepository.findUserByEmail(email).isPresent();
    }

    @Override
    public boolean isUsernameTaken(String username) {
        return userRepository.findUserByUsername(username).isPresent();
    }

    @Override
    public boolean isUserNumberTaken(String userNumber) {
        return userRepository.findUserByUserNumber(userNumber).isPresent();
    }

    @Override
    public void enableUser(String email) {
        var user = findExistingUserByEmail(email);
        user.setIsEnabled(true);
        userRepository.save(user);
    }

    private void validateNewUsernameAndEmail(RegistrationRequest registrationRequest)
            throws UsernameExistException, EmailExistException {
        boolean isEmailBeTaken = isEmailTaken(registrationRequest.getEmail());
        boolean isUsernameBeTaken = isUsernameTaken(registrationRequest.getUsername());
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
            isUserNumberTaken = isUserNumberTaken(userNumber);
        } while (isUserNumberTaken);

        return userNumber;
    }

    private String getTemporaryProfileImageUrl(String username) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(DEFAULT_USER_IMAGE_PATH + username)
                .toUriString();
    }

    private void validateLoginAttempt(User user) {
        var username = user.getUsername();
        if(user.getIsAccountNonLocked()) {
            if(loginAttemptService.hasExceededMaxAttempts(username))
                user.setIsAccountNonLocked(false);
        }
        else
            loginAttemptService.removeUserFromLoginAttemptCache(username);
    }
}
