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
    public Iterable<User> findAllUsers(){
        return userService.findAllUsers();
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<User> findOneById(@PathVariable Long id){
        Optional<User> optUser = userService.findUserById(id);

        return optUser.map(
                user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElseGet(
                        () -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND)
                );
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody User user){
        return userService.createUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User user){
        return userService.updateUser(user);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id){
        userService.deleteUserById(id);
    }
}
