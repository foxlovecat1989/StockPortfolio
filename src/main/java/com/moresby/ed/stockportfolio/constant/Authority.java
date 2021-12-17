package com.moresby.ed.stockportfolio.constant;

public class Authority {
    public static final String[] USER_AUTHORITIES = {
            "user:read",
            "stock:read"
    };
    public static final String[] MANAGER_AUTHORITIES = {
            "user:read", "user:create", "user:update",
            "stock:read", "stock:create", "stock:update"
    };
    public static final String[] ADMIN_AUTHORITIES = {
            "user:read", "user:create", "user:update", "user:delete",
            "stock:read", "stock:create", "stock:update", "stock:delete"
    };
}
