package com.moresby.ed.stockportfolio.service.impl;

import com.moresby.ed.stockportfolio.domain.User;
import com.moresby.ed.stockportfolio.domain.UserPrincipal;
import com.moresby.ed.stockportfolio.repository.UserRepository;
import com.moresby.ed.stockportfolio.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.moresby.ed.stockportfolio.constant.UserImplConstant.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

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
        user.setLastLoginDateDisplay(user.getLastLoginDate());
        user.setLastLoginDate(new Date());
        userRepository.save(user);
        UserPrincipal userPrincipal = new UserPrincipal(user);
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
        user.setIsEnabled(Boolean.TRUE);
        userRepository.save(user);
    }

}
