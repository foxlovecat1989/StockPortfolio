package com.moresby.ed.stockportfolio.user.registration.token;

import com.moresby.ed.stockportfolio.user.User;

import java.util.Optional;

public interface ConfirmEmailTokenService {
    Optional<ConfirmEmailToken> findByToken(String token);
    ConfirmEmailToken createToken(User user);
}
