package com.moresby.ed.stockportfolio.email;

import com.moresby.ed.stockportfolio.user.User;

public interface EmailService {
    void sendConfirmEmail(User user);
    void submit() throws Exception;
}
