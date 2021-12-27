package com.moresby.ed.stockportfolio.service;

import com.moresby.ed.stockportfolio.domain.RegistrationRequest;
import com.moresby.ed.stockportfolio.domain.User;
import com.moresby.ed.stockportfolio.exception.domain.user.*;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService extends UserDetailsService {
    User findExistingUserById(Long id) throws UserNotFoundException;
    User findExistingUserByUserNumber(String userNumber) throws UserNotFoundException;
    User findExistingUserByUsername(String username) throws UserNotFoundException;
    User findExistingUserByEmail(String email) throws UserNotFoundException;
    List<User> findAllUsers();
    User register(RegistrationRequest registrationRequest) throws EmailExistException, UsernameExistException;
    User createUser(User user) throws EmailExistException, UsernameExistException;
    User updateUser(User user) throws EmailExistException, UsernameExistException, UserNotFoundException;
    User updateUserNameOrEmail(User user) throws UserNotFoundException, EmailExistException, UsernameExistException;
    void deleteUserByUserNumber(String userNumber) throws UserNotFoundException;
    boolean isEmailTaken(String email);
    boolean isUsernameTaken(String username);
    boolean isUserNumberTaken(String userNumber);
    void enableUser(String email) throws UserNotFoundException;
    void resetPassword(String email) throws UserNotFoundException;
    User updateProfileImage(String username, MultipartFile multipartFile)
            throws EmailExistException, UsernameExistException, IOException, NotAnImageFileException, UserNotFoundException;
}
