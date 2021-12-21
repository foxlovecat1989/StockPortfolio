package com.moresby.ed.stockportfolio.constant;

public class Authority {
    public static final String[] USER_AUTHORITIES = {
            "user:read",
            "trade:read",
            "stock:read"
    };
    public static final String[] MANAGER_AUTHORITIES = {
            "user:read", "user:create", "user:update",
            "trade:read", "trade:create", "trade:update",
            "stock:read", "stock:create", "stock:update"
    };
    public static final String[] ADMIN_AUTHORITIES = {
            "user:read", "user:create", "user:update", "user:delete",
            "trade:read", "trade:create", "trade:update", "trade:delete",
            "stock:read", "stock:create", "stock:update", "stock:delete"
    };
}
