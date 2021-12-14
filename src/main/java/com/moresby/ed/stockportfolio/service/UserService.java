package com.moresby.ed.stockportfolio.service;

import com.moresby.ed.stockportfolio.domain.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {
    User findExistingUserById(Long id);
    Optional<User> findById(long id);
    List<User> findAllUsers();
    User createUser(User user);
    User updateUser(User user);
    void deleteUserById(Long id);
    Boolean isEmailTaken(String email);
    void enableUser(String email);
}
