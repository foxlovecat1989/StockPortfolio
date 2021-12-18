package com.moresby.ed.stockportfolio.resource;

import com.moresby.ed.stockportfolio.domain.HttpResponse;
import com.moresby.ed.stockportfolio.domain.User;
import com.moresby.ed.stockportfolio.domain.UserPrincipal;
import com.moresby.ed.stockportfolio.exception.ExceptionHandling;
import com.moresby.ed.stockportfolio.exception.domain.EmailExistException;
import com.moresby.ed.stockportfolio.exception.domain.NotAnImageFileException;
import com.moresby.ed.stockportfolio.exception.domain.UsernameExistException;
import com.moresby.ed.stockportfolio.service.UserService;
import com.moresby.ed.stockportfolio.utility.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


import static com.moresby.ed.stockportfolio.constant.FileConstant.*;
import static com.moresby.ed.stockportfolio.constant.SecurityConstant.JWT_TOKEN_HEADER;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;

@RestController
@RequestMapping(path = "/api/v1/user")
@RequiredArgsConstructor
@Slf4j
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

    @PostMapping("/updateProfileImage")
    public ResponseEntity<User> updateProfileImage(
            @RequestParam("username") String username,
            @RequestParam(value = "profileImage") MultipartFile profileImage
    )
            throws
            UsernameExistException,
            EmailExistException,
            IOException,
            NotAnImageFileException {
        User user = userService.updateProfileImage(username, profileImage);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PatchMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<User> updateUserName(@RequestBody User user)
            throws InterruptedException, EmailExistException, UsernameExistException {
        Thread.sleep(3000); // TODO: remove when production
        var updateUser = userService.updateUsername(user);

        return new ResponseEntity<>(updateUser, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    @PreAuthorize("hasAnyAuthority('user:delete')")
    public ResponseEntity<HttpResponse> deleteUser(@PathVariable String username) throws InterruptedException {
        Thread.sleep(3000); // TODO: remove when production
        userService.deleteUserByUsername(username);

        return response(HttpStatus.NO_CONTENT, USER_DELETED_SUCCESSFULLY);
    }

    @GetMapping(path = "/image/{username}/{fileName}", produces = IMAGE_JPEG_VALUE)
    public byte[] getProfileImage(
            @PathVariable("username") String username,
            @PathVariable("fileName") String fileName) throws IOException {

        return Files.readAllBytes(Paths.get(USER_FOLDER + username + FORWARD_SLASH + fileName));
    }

    @GetMapping(path = "/image/profile/{username}", produces = IMAGE_JPEG_VALUE)
    public byte[] getTempProfileImage(@PathVariable("username") String username) throws IOException {
        URL url = new URL(TEMP_PROFILE_IMAGE_BASE_URL + username);
        log.info(url.toString());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (InputStream inputStream = url.openStream()) {
            int bytesRead;
            byte[] chunk = new byte[1024];
            while((bytesRead = inputStream.read(chunk)) > 0) {
                byteArrayOutputStream.write(chunk, 0, bytesRead);
            }
        }

        return byteArrayOutputStream.toByteArray();
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
