package com.moresby.ed.stockportfolio.User;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping(path = "/findAll", produces = "application/json")
    public Iterable<User> findAllUsers() throws InterruptedException {
        Thread.sleep(3000); // TODO: remove when production

        return userService.findAllUsers();
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<User> findOneById(@PathVariable Long id) throws InterruptedException {
        Thread.sleep(3000); // TODO: remove when production
        Optional<User> optUser = userService.findUserById(id);

        return optUser.map(
                user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElseGet(
                        () -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND)
                );
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody User user) throws InterruptedException {
        Thread.sleep(3000); // TODO: remove when production

        return userService.createUser(user);
    }

    @PatchMapping
    public User updateUser(@RequestBody User user) throws InterruptedException {
        Thread.sleep(3000); // TODO: remove when production

        return userService.updateUser(user);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) throws InterruptedException {
        Thread.sleep(3000); // TODO: remove when production
        userService.deleteUserById(id);
    }

    @PostMapping(path = "/register/check")
    public Boolean isEmailTaken(@RequestBody User user){

        return userService.isEmailTaken(user.getEmail());
    }
}
