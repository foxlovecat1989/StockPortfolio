package com.moresby.ed.stockportfolio.user;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

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
    public Iterable<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public Iterable<User> createUsers(Iterable<User> users) {
        return userRepository.saveAll(users);
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

        return userRepository.findUserByEmailEquals(email).isPresent();
    }


}
