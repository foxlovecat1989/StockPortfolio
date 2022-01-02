package com.moresby.ed.stockportfolio.resource;

import com.moresby.ed.stockportfolio.domain.HttpResponse;
import com.moresby.ed.stockportfolio.domain.User;
import com.moresby.ed.stockportfolio.domain.UserPrincipal;
import com.moresby.ed.stockportfolio.exception.domain.user.*;
import com.moresby.ed.stockportfolio.exception.handler.UserExceptionHandling;
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
import java.util.List;

import static com.moresby.ed.stockportfolio.constant.SecurityConstant.JWT_TOKEN_HEADER;
import static com.moresby.ed.stockportfolio.constant.UserImplConstant.EMAIL_SENT;
import static com.moresby.ed.stockportfolio.constant.UserImplConstant.USER_DELETED_SUCCESSFULLY;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/api/v1/user")
@RequiredArgsConstructor
@Slf4j
public class UserController extends UserExceptionHandling {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    @GetMapping(path = "/findAll", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> findAllUsers() throws InterruptedException {
        Thread.sleep(3000); // TODO: remove when production
        List<User> users = userService.findAllUsers();

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping(path = "/{userNumber}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<User> findUserByUserNumber(@PathVariable String userNumber) throws InterruptedException, UserNotFoundException {
        Thread.sleep(3000); // TODO: remove when production
        var user = userService.findExistingUserByUserNumber(userNumber);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user)
            throws InterruptedException, EmailExistException, UsernameExistException {
        Thread.sleep(3000); // TODO: remove when production
        var newUser = userService.createUser(user);

        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

//    @PostMapping("/updateProfileImage/{userNumber}")
//    public ResponseEntity<User> updateProfileImage(
//            @PathVariable("userNumber") String userNumber,
//            @RequestParam(value = "profileImage") MultipartFile profileImage
//    )
//            throws
//            UsernameExistException,
//            EmailExistException,
//            IOException,
//            NotAnImageFileException, UserNotFoundException {
//        User user = userService.updateProfileImage(userNumber, profileImage);
//
//        return new ResponseEntity<>(user, HttpStatus.OK);
//    }

    @PatchMapping(path = "/update", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<User> updateUser(@RequestBody User user)
            throws InterruptedException, EmailExistException, UsernameExistException, UserNotFoundException {
        Thread.sleep(3000); // TODO: remove when production
        var updateUser = userService.updateUser(user);

        return new ResponseEntity<>(updateUser, HttpStatus.OK);
    }

    @PatchMapping(path = "/modified", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<User> updateUserNameOrEmail(@RequestBody User user)
            throws InterruptedException, EmailExistException, UsernameExistException, UserNotFoundException {
        Thread.sleep(3000); // TODO: remove when production
        var updateUser = userService.updateUserNameOrEmail(user);

        return new ResponseEntity<>(updateUser, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{userNumber}")
    @PreAuthorize("hasAnyAuthority('user:delete')")
    public ResponseEntity<HttpResponse> deleteUser(@PathVariable("userNumber") String userNumber)
            throws InterruptedException, UserNotFoundException {
        Thread.sleep(3000); // TODO: remove when production
        userService.deleteUserByUserNumber(userNumber);

        return response(HttpStatus.NO_CONTENT, USER_DELETED_SUCCESSFULLY);
    }

//    @GetMapping(path = "/image/{username}/{fileName}", produces = IMAGE_JPEG_VALUE)
//    public byte[] getProfileImage(
//            @PathVariable("username") String username,
//            @PathVariable("fileName") String fileName) throws IOException {
//
//        return Files.readAllBytes(Paths.get(USER_FOLDER + username + FORWARD_SLASH + fileName));
//    }
//
//    @GetMapping(path = "/image/profile/{username}", produces = IMAGE_JPEG_VALUE)
//    public byte[] getTempProfileImage(@PathVariable("username") String username) throws IOException {
//        URL url = new URL(TEMP_PROFILE_IMAGE_BASE_URL + username);
//        log.info(url.toString());
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        try (InputStream inputStream = url.openStream()) {
//            int bytesRead;
//            byte[] chunk = new byte[1024];
//            while((bytesRead = inputStream.read(chunk)) > 0) {
//                byteArrayOutputStream.write(chunk, 0, bytesRead);
//            }
//        }
//
//        return byteArrayOutputStream.toByteArray();
//    }

    @GetMapping(path = "/reset/password/{email}")
    public ResponseEntity<HttpResponse> resetPassword(@PathVariable("email") String email)
            throws InterruptedException, UserNotFoundException {
        Thread.sleep(3000); // TODO: remove when production
        System.out.println(email);

        userService.resetPassword(email);

        return response(HttpStatus.OK, EMAIL_SENT + email);
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User user) throws UserNotFoundException {
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
