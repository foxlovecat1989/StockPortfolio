package com.moresby.ed.stockportfolio.service;

import com.moresby.ed.stockportfolio.domain.RegistrationRequest;
import com.moresby.ed.stockportfolio.domain.User;
import com.moresby.ed.stockportfolio.exception.domain.EmailExistException;
import com.moresby.ed.stockportfolio.exception.domain.UsernameExistException;

public interface RegistrationService {
    User registration(RegistrationRequest registrationRequest) throws UsernameExistException, EmailExistException;
}
