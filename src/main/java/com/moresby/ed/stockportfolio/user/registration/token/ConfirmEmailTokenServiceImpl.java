package com.moresby.ed.stockportfolio.user.registration.token;

import com.moresby.ed.stockportfolio.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ConfirmEmailTokenServiceImpl implements ConfirmEmailTokenService{

    private final static long EXPIRATION_TIME_IN_FIFTEEN_MINUTES = 15L;
    private final ConfirmEmailTokenRepository confirmEmailTokenRepository;

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
                        .user(user)
                        .build();

        return confirmEmailTokenRepository.save(confirmEmailToken);
    }
}
