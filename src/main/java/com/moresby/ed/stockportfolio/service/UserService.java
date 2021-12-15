package com.moresby.ed.stockportfolio.service;

import com.moresby.ed.stockportfolio.domain.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import java.util.List;

public interface UserService extends UserDetailsService {
    User findExistingUserById(Long id);
    User findExistingUserByUsername(String username);
    User findExistingUserByEmail(String email);
    List<User> findAllUsers();
    User createUser(User user);
    User updateUser(User user);
    void deleteUserById(Long id);
    boolean isEmailTaken(String email);
    boolean isUsernameTaken(String username);
    boolean isUserNumberTaken(String userNumber);
    void enableUser(String email);
}
