package com.moresby.ed.stockportfolio.user.registration;

import com.moresby.ed.stockportfolio.user.User;

public interface RegistrationService {
    User registration(RegistrationRequest registrationRequest);
}
