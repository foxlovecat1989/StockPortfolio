package com.moresby.ed.stockportfolio.service.impl;

import com.moresby.ed.stockportfolio.domain.Account;
import com.moresby.ed.stockportfolio.domain.RegistrationRequest;
import com.moresby.ed.stockportfolio.domain.User;
import com.moresby.ed.stockportfolio.domain.UserPrincipal;
import com.moresby.ed.stockportfolio.enumeration.UserRole;
import com.moresby.ed.stockportfolio.exception.domain.EmailExistException;
import com.moresby.ed.stockportfolio.exception.domain.NotAnImageFileException;
import com.moresby.ed.stockportfolio.exception.domain.UsernameExistException;
import com.moresby.ed.stockportfolio.repository.UserRepository;
import com.moresby.ed.stockportfolio.service.EmailService;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static com.moresby.ed.stockportfolio.constant.FileConstant.*;
import static com.moresby.ed.stockportfolio.constant.UserImplConstant.*;
import static com.moresby.ed.stockportfolio.constant.UserImplConstant.EMAIL_ALREADY_EXISTS;
import static com.moresby.ed.stockportfolio.constant.UserImplConstant.USERNAME_ALREADY_EXISTS;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.springframework.http.MediaType.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final EmailService emailService;
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

        return userRepository.findAll();
    }

    @Override
    public User createUser(User user) throws EmailExistException, UsernameExistException {
        validateNewUsernameAndEmail(user.getUsername(), user.getEmail());

        return buildUser(user);
    }

    @Override
    public User register(RegistrationRequest registrationRequest) throws EmailExistException, UsernameExistException {
        var username = registrationRequest.getUsername();
        var email = registrationRequest.getEmail();

        validateNewUsernameAndEmail(username, email);
        var newUser =
                User.builder()
                        .username(username)
                        .email(email)
                        .password(registrationRequest.getPassword())
                        .build();

        return buildUser(newUser);
    }

    // TODO: back to set what properties should be updated

    @Override
    public User updateUsername(User user) throws EmailExistException, UsernameExistException {
        validateNewUsernameAndEmail(user.getUsername(), null);
        var existingUser = findExistingUserByEmail(user.getEmail());
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

    @Override
    public void resetPassword(String email) {
        var originUser = findExistingUserByEmail(email);
        var newPassword = generatePassword();
        originUser.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(originUser);

        emailService.sendNewPasswordEmail(originUser, newPassword);
    }

    @Override
    public User updateProfileImage(String username, MultipartFile multipartFile)
            throws IOException, NotAnImageFileException {
        var user = findExistingUserByUsername(username);
        saveProfileImage(user, multipartFile);

        return user;
    }

    private User buildUser(User user) {
        var FIVE_MILLION = 5_000_000;
        Account account =
                Account.builder()
                        .balance(BigDecimal.valueOf(FIVE_MILLION))
                        // TODO: WHEN PRODUCTION Change to .balance(BigDecimal.ZERO)
                        .build();
        User newUser =
                User.builder()
                        .userNumber(generateUserNumber())
                        .username(user.getUsername())
                        .password(passwordEncoder.encode(user.getPassword()))
                        .email(user.getEmail())
                        .userRole(UserRole.ROLE_USER)
                        .joinDate(new Date())
                        // TODO: de-comment when production
                        .profileImageUrl(getTemporaryProfileImageUrl(user.getUsername()))
                        .isEnabled(true)
                        // TODO: set .isEnabled(false) when production
                        .isAccountNonLocked(true)
                        .account(account)
                        .build();

        account.setUser(newUser);

        return userRepository.save(newUser);
    }

    private void validateNewUsernameAndEmail(String username, String email)
            throws UsernameExistException, EmailExistException {
        boolean isEmailBeTaken = isEmailTaken(email);
        boolean isUsernameBeTaken = isUsernameTaken(username);
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

    private void validateLoginAttempt(User user) {
        var username = user.getUsername();
        if(user.getIsAccountNonLocked()) {
            if(loginAttemptService.hasExceededMaxAttempts(username))
                user.setIsAccountNonLocked(false);
        }
        else
            loginAttemptService.removeUserFromLoginAttemptCache(username);
    }

    private String generatePassword() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    private void saveProfileImage(User user, MultipartFile profileImage) throws IOException, NotAnImageFileException {
        if (profileImage != null) {
            if(!Arrays.asList(IMAGE_JPEG_VALUE, IMAGE_PNG_VALUE, IMAGE_GIF_VALUE).contains(profileImage.getContentType()))
                throw new NotAnImageFileException(profileImage.getOriginalFilename() + NOT_AN_IMAGE_FILE);

            Path userFolder = Paths.get(USER_FOLDER + user.getUsername()).toAbsolutePath().normalize();
            if(!Files.exists(userFolder)) {
                Files.createDirectories(userFolder);
                log.info(DIRECTORY_CREATED + userFolder);
            }
            Files.deleteIfExists(Paths.get(userFolder + user.getUsername() + DOT + JPG_EXTENSION));
            Files.copy(
                    profileImage.getInputStream(),
                    userFolder.resolve(user.getUsername() + DOT + JPG_EXTENSION), REPLACE_EXISTING
            );
            user.setProfileImageUrl(setProfileImageUrl(user.getUsername()));
            userRepository.save(user);
            log.info(FILE_SAVED_IN_FILE_SYSTEM + profileImage.getOriginalFilename());
        }
    }

    private String getTemporaryProfileImageUrl(String username) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(DEFAULT_USER_IMAGE_PATH + username)
                .toUriString();
    }

    private String setProfileImageUrl(String username) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(USER_IMAGE_PATH + username + FORWARD_SLASH + username + DOT + JPG_EXTENSION)
                .toUriString();
    }

}
