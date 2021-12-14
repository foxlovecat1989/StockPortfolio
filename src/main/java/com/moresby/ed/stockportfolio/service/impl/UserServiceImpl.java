package com.moresby.ed.stockportfolio.service.impl;

import com.moresby.ed.stockportfolio.domain.User;
import com.moresby.ed.stockportfolio.repository.UserRepository;
import com.moresby.ed.stockportfolio.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final static String USER_NOT_FOUND_MSG = "User with email: %s NOT FOUND";
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findUserByEmail(email)
                .orElseThrow(()-> {
                    var userEmailNotFoundException =
                            new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email));
                    log.warn("Error in LoadUserByEmail: {}", email, userEmailNotFoundException);
                    return userEmailNotFoundException;
                });
    }

    @Override
    public User findExistingUserById(Long id) {

        return userRepository.findById(id)
                .orElseThrow(
                        () -> new NoSuchElementException(String.format("User Id: %s Not Found", id))
                );
    }

    @Override
    public Optional<User> findById(long id) {
        return userRepository.findById(id);
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
    public User updateUser(User user) {
        User originUser = userRepository.findById(user.getId())
                    .orElseThrow(
                            ()-> new IllegalStateException("update user fail Exception")
                    );
        originUser.setUsername(
                user.getUsername() != null ? user.getUsername() : originUser.getUsername()
        );

        return userRepository.save(originUser);
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
    public Boolean isEmailTaken(String email) {

        return userRepository.findUserByEmail(email).isPresent();
    }

    @Override
    public void enableUser(String email) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(
                        ()-> new IllegalStateException(String.format("Email: %s was Not Found", email))
                );
        user.setIsEnabled(Boolean.TRUE);
        userRepository.save(user);
    }

}
