package com.moresby.ed.stockportfolio.user;

import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService extends UserDetailsService {
    User findExistingUserById(Long id);
    Optional<User> findById(long id);
    Iterable<User> findAllUsers();
    User createUser(User user);
    User updateUser(User user);
    void deleteUserById(Long id);
    Boolean isEmailTaken(String email);
    void enableUser(String email);
}
