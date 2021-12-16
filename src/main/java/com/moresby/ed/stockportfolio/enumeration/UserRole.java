package com.moresby.ed.stockportfolio.enumeration;

import com.google.common.collect.Sets;

import java.util.Set;

import static com.moresby.ed.stockportfolio.constant.Authority.*;

public enum UserRole {
    ROLE_USER(USER_AUTHORITIES),
    ROLE_MANAGER(MANAGER_AUTHORITIES),
    ROLE_ADMIN(ADMIN_AUTHORITIES);

    private Set<String> authorities;

    UserRole(String... authorities) {
        this.authorities =  Sets.newHashSet(authorities);
    }

    public Set<String> getAuthorities() {
        return authorities;
    }
}
