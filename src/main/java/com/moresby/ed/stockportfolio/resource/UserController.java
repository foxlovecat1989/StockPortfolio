package com.moresby.ed.stockportfolio.resource;

import com.moresby.ed.stockportfolio.domain.HttpResponse;
import com.moresby.ed.stockportfolio.domain.User;
import com.moresby.ed.stockportfolio.domain.UserPrincipal;
import com.moresby.ed.stockportfolio.exception.ExceptionHandling;
import com.moresby.ed.stockportfolio.exception.domain.EmailExistException;
import com.moresby.ed.stockportfolio.exception.domain.UsernameExistException;
import com.moresby.ed.stockportfolio.service.UserService;
import com.moresby.ed.stockportfolio.utility.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import java.util.List;


import static com.moresby.ed.stockportfolio.constant.SecurityConstant.JWT_TOKEN_HEADER;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/api/v1/user")
@RequiredArgsConstructor
public class UserController extends ExceptionHandling {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    public static final String EMAIL_SENT = "An email with a new password was sent to: ";
    public static final String USER_DELETED_SUCCESSFULLY = "User deleted successfully";

    @GetMapping(path = "/findAll", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> findAllUsers() throws InterruptedException {
        Thread.sleep(3000); // TODO: remove when production
        List<User> users = userService.findAllUsers();

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<User> findOneById(@PathVariable Long id) throws InterruptedException {
        Thread.sleep(3000); // TODO: remove when production
        User user = userService.findExistingUserById(id);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user)
            throws InterruptedException, EmailExistException, UsernameExistException {
        Thread.sleep(3000); // TODO: remove when production
        var newUser = userService.createUser(user);

        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @PatchMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<User> updateUser(@RequestBody User user)
            throws InterruptedException, EmailExistException, UsernameExistException {
        Thread.sleep(3000); // TODO: remove when production
        var updateUser = userService.updateUsername(user);

        return new ResponseEntity<>(updateUser, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    @PreAuthorize("hasAnyAuthority('user:delete')")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public ResponseEntity<HttpResponse> deleteUser(@PathVariable Long id) throws InterruptedException {
        Thread.sleep(3000); // TODO: remove when production
        userService.deleteUserById(id);

        return response(HttpStatus.OK, USER_DELETED_SUCCESSFULLY);
    }

    @GetMapping(path = "/resetPassword/{email}")
    public ResponseEntity<HttpResponse> restPassword(@PathVariable("email") String email) throws InterruptedException {
        Thread.sleep(3000); // TODO: remove when production
        userService.resetPassword(email);

        return response(HttpStatus.OK, EMAIL_SENT + email);
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User user) {
        authenticate(user.getUsername(), user.getPassword());
        var loginUser = userService.findExistingUserByUsername(user.getUsername());
        UserPrincipal userPrincipal = new UserPrincipal(loginUser);
        HttpHeaders jwtHeader = getJwtHeader(userPrincipal);

        return new ResponseEntity<>(loginUser, jwtHeader, HttpStatus.OK);
    }

    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

    private HttpHeaders getJwtHeader(UserPrincipal userPrincipal) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(JWT_TOKEN_HEADER, jwtTokenProvider.generateJwtToken(userPrincipal));

        return headers;
    }
    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(
                new HttpResponse(
                        httpStatus.value(),
                        httpStatus,
                        httpStatus.getReasonPhrase().toUpperCase(),
                        message),
                httpStatus);
    }
}
