package com.moresby.ed.stockportfolio.user;

import com.moresby.ed.stockportfolio.user.User;

import java.util.Optional;

public interface UserService {
    User findExistingUserById(Long id);
    Optional<User> findById(long id);
    Iterable<User> findAllUsers();
    User createUser(User user);
    Iterable<User> createUsers(Iterable<User> users);
    User updateUser(User user);
    void deleteUserById(Long id);
    Boolean isEmailTaken(String email);
}
