package com.moresby.ed.stockportfolio.resource;

import com.moresby.ed.stockportfolio.domain.User;
import com.moresby.ed.stockportfolio.exception.ExceptionHandling;
import com.moresby.ed.stockportfolio.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/api/v1/user")
@RequiredArgsConstructor
public class UserController extends ExceptionHandling {

    private final UserService userService;
    private static final int INDEX_OF_ROLE_ENDS = 5;
    private static final int CLEAN_COOKIE_SET_TO_ZERO = 0;

    @GetMapping(path = "/findAll", produces = APPLICATION_JSON_VALUE)
    public Iterable<User> findAllUsers() throws InterruptedException {
        Thread.sleep(3000); // TODO: remove when production

        return userService.findAllUsers();
    }

    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<User> findOneById(@PathVariable Long id) throws InterruptedException {
        Thread.sleep(3000); // TODO: remove when production
        User user = userService.findExistingUserById(id);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody User user) throws InterruptedException {
        Thread.sleep(3000); // TODO: remove when production

        return userService.createUser(user);
    }

    @PatchMapping(consumes = APPLICATION_JSON_VALUE)
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

    @GetMapping("/currentUserRole")
    public Map<String, String> getCurrentUserRole(){
        Collection<GrantedAuthority> roles =
                (Collection<GrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        String role = "";
        if(roles.size() > 0){
            GrantedAuthority grantedAuthority = roles.iterator().next();
            role = grantedAuthority.getAuthority().substring(INDEX_OF_ROLE_ENDS);
        }
        Map<String, String> results = new HashMap<>();
        results.put("role", role);

        return results;
    }

    @GetMapping(path = "/logout")
    public String logout(HttpServletResponse response){
        Cookie cookie = new Cookie("token", null);
        cookie.setPath("/api");
        cookie.setHttpOnly(true);
        // TODO: when in production must do cookie.setSecure(true);
        cookie.setMaxAge(CLEAN_COOKIE_SET_TO_ZERO);
        SecurityContextHolder.getContext().setAuthentication(null);

        return "";
    }

}
