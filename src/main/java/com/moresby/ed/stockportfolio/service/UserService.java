package com.moresby.ed.stockportfolio.service;

import com.moresby.ed.stockportfolio.domain.RegistrationRequest;
import com.moresby.ed.stockportfolio.domain.User;
import com.moresby.ed.stockportfolio.exception.domain.user.EmailExistException;
import com.moresby.ed.stockportfolio.exception.domain.user.NotAnImageFileException;
import com.moresby.ed.stockportfolio.exception.domain.user.UsernameExistException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService extends UserDetailsService {
    User findExistingUserById(Long id);
    User findExistingUserByUsername(String username);
    User findExistingUserByEmail(String email);
    List<User> findAllUsers();
    User register(RegistrationRequest registrationRequest) throws EmailExistException, UsernameExistException;
    User createUser(User user) throws EmailExistException, UsernameExistException;
    User updateUsername(User user) throws EmailExistException, UsernameExistException;
    void deleteUserByUsername(String username);
    boolean isEmailTaken(String email);
    boolean isUsernameTaken(String username);
    boolean isUserNumberTaken(String userNumber);
    void enableUser(String email);
    void resetPassword(String email);
    User updateProfileImage(String username, MultipartFile multipartFile) throws EmailExistException, UsernameExistException, IOException, NotAnImageFileException;
}
