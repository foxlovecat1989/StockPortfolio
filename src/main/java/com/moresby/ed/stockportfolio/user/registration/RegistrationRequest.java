package com.moresby.ed.stockportfolio.user.registration;

import lombok.Data;

@Data
public class RegistrationRequest {
    private final String username;
    private final String password;
    private final String email;

}
