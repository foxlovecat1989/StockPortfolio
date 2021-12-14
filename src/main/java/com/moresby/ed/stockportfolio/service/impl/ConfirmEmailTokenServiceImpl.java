package com.moresby.ed.stockportfolio.service.impl;

import com.moresby.ed.stockportfolio.domain.ConfirmEmailToken;
import com.moresby.ed.stockportfolio.repository.ConfirmEmailTokenRepository;
import com.moresby.ed.stockportfolio.service.ConfirmEmailTokenService;
import com.moresby.ed.stockportfolio.service.EmailService;
import com.moresby.ed.stockportfolio.domain.User;
import com.moresby.ed.stockportfolio.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class ConfirmEmailTokenServiceImpl implements ConfirmEmailTokenService {

    private final static long EXPIRATION_TIME_IN_FIFTEEN_MINUTES = 15L;
    private final ConfirmEmailTokenRepository confirmEmailTokenRepository;
    private final UserService userService;
    private final EmailService emailService;

    @Override
    public Optional<ConfirmEmailToken> findByToken(String token) {
        return confirmEmailTokenRepository.findByToken(token);
    }

    @Override
    public ConfirmEmailToken createToken(User user) {
        String token = UUID.randomUUID().toString();
        ConfirmEmailToken confirmEmailToken =
                ConfirmEmailToken.builder()
                        .createdAt(LocalDateTime.now())
                        .expiresAt(LocalDateTime.now().plusMinutes(EXPIRATION_TIME_IN_FIFTEEN_MINUTES))
                        .token(token)
                        .email(user.getEmail())
                        .user(user)
                        .build();
        user.addConfirmEmailToken(confirmEmailToken);
        emailService.sendConfirmEmail(user);

        return confirmEmailTokenRepository.save(confirmEmailToken);
    }

    @Override
    public String confirmToken(String token) {
        // get token from db
        ConfirmEmailToken confirmEmailToken = getToken(token).orElseThrow(
                () -> {
                    String errorMsg = "Token: %s Not Found";
                    IllegalStateException exception = new IllegalStateException(String.format(errorMsg, token));
                    log.error(String.format(errorMsg, token), exception);

                    return exception;
                }
        );

        // examine the token whether is already confirmed or not
        boolean isAlreadyConfirm = confirmEmailToken.getConfirmedAt() != null;
        if (isAlreadyConfirm){
            log.warn(String.format("Token: %s is already confirmed", token));
            throw new IllegalStateException(String.format("Token: %s is already confirmed", token));
        }

        // check the token whether is already expired or not
        LocalDateTime expiresAt = confirmEmailToken.getExpiresAt();
        boolean isExpired = expiresAt.isBefore(LocalDateTime.now());
        if (isExpired){
            log.warn(String.format("Token: %s is already expired", token));
            throw new IllegalStateException(String.format("Token: %s is already expired", token));
        }

        setConfirmedAt(confirmEmailToken);
        userService.enableUser(confirmEmailToken.getEmail());

        return "success";
    }

    @Override
    public Optional<ConfirmEmailToken> getToken(String token) {
        return confirmEmailTokenRepository.findByToken(token);
    }

    @Override
    public void setConfirmedAt(ConfirmEmailToken confirmEmailToken){
        confirmEmailToken.setConfirmedAt(LocalDateTime.now());
        confirmEmailTokenRepository.save(confirmEmailToken);
    }
}
