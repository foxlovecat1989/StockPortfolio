package com.moresby.ed.stockportfolio.domain;

import lombok.*;

@Getter
@Setter
public class RegistrationRequest {
    private final String username;
    private final String password;
    private final String email;

    @Builder
    public RegistrationRequest(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
}
