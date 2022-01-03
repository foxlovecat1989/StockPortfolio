package com.moresby.ed.stockportfolio.constant;

public class Authority {
    public static final String[] USER_AUTHORITIES = {
            "user:read", "user:create", "user:update",
            "account:read",
            "trade:read", "trade:create", "trade:update",
            "stock:read", "stock:create", "stock:update",
            "watchlist:read", "watchlist:create", "watchlist:update", "watchlist:delete",
            "inventory:read",
    };
    public static final String[] MANAGER_AUTHORITIES = {
            "user:read", "user:create", "user:update", "user:delete",
            "trade:read", "trade:create", "trade:update", "trade:delete",
            "stock:read", "stock:create", "stock:update", "stock:delete",
            "watchlist:read", "watchlist:create", "watchlist:update", "watchlist:delete",
            "inventory:read",
            "manage:read", "manage:create", "manage:update", "manage:delete",
    };
    public static final String[] ADMIN_AUTHORITIES = {
            "user:read", "user:create", "user:update", "user:delete",
            "trade:read", "trade:create", "trade:update", "trade:delete",
            "stock:read", "stock:create", "stock:update", "stock:delete",
            "watchlist:read", "watchlist:create", "watchlist:update", "watchlist:delete",
            "inventory:read",
            "admin:read", "admin:create", "admin:update", "admin:delete",
    };
}
