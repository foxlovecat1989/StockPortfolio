package com.moresby.ed.stockportfolio.service;

import com.moresby.ed.stockportfolio.domain.User;

public interface EmailService {
    void sendConfirmEmail(User user);
    void sendNewPasswordEmail(User user);
    void submit() throws Exception;
}
