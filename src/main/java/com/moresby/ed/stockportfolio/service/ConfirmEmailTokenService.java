package com.moresby.ed.stockportfolio.service;

import com.moresby.ed.stockportfolio.domain.ConfirmEmailToken;
import com.moresby.ed.stockportfolio.domain.User;
import com.moresby.ed.stockportfolio.exception.domain.user.UserNotFoundException;

import java.util.Optional;

public interface ConfirmEmailTokenService {
    Optional<ConfirmEmailToken> findByToken(String token);
    ConfirmEmailToken createToken(User user);
    String confirmToken(String token) throws UserNotFoundException;
    Optional<ConfirmEmailToken> getToken(String token);
    void setConfirmedAt(ConfirmEmailToken confirmEmailToken);
}
