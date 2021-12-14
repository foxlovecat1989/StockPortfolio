package com.moresby.ed.stockportfolio.service;

import com.moresby.ed.stockportfolio.domain.RegistrationRequest;
import com.moresby.ed.stockportfolio.domain.User;

public interface RegistrationService {
    User registration(RegistrationRequest registrationRequest);
}
