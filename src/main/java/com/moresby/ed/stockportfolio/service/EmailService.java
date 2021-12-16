package com.moresby.ed.stockportfolio.service;

import com.moresby.ed.stockportfolio.domain.User;

public interface EmailService {
    void sendConfirmEmail(User user);
    void sendNewPasswordEmail(User user, String password);
    void submit() throws Exception;
}
