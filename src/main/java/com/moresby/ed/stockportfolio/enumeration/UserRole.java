package com.moresby.ed.stockportfolio.enumeration;


import static com.moresby.ed.stockportfolio.constant.Authority.*;

public enum UserRole {
    ROLE_USER(USER_AUTHORITIES),
    ROLE_MANAGER(MANAGER_AUTHORITIES),
    ROLE_ADMIN(ADMIN_AUTHORITIES);

    private String[] authorities;

    UserRole(String... authorities) {
        this.authorities = authorities;
    }

    public String[] getAuthorities() {
        return authorities;
    }
}
