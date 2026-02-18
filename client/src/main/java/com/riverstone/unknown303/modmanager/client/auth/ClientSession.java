package com.riverstone.unknown303.modmanager.client.auth;

import com.riverstone.unknown303.modmanager.common.user.UserIdentity;

public class ClientSession {
    private UserIdentity identity;
    private String token;

    public void set(UserIdentity identity, String token) {
        this.identity = identity;
        this.token = token;
    }

    public UserIdentity getIdentity() {
        return identity;
    }

    public String getToken() {
        return token;
    }

    public boolean isLoggedIn() {
        return identity != null;
    }
}
