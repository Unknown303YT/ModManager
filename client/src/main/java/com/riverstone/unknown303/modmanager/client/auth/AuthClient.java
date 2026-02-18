package com.riverstone.unknown303.modmanager.client.auth;

import com.riverstone.unknown303.modmanager.common.user.UserIdentity;

public final class AuthClient {
    public static final AuthClient INSTANCE = new AuthClient();

    private final ClientSession session = new ClientSession();

    public void onLoginSuccess(UserIdentity identity, String token) {

    }

    public ClientSession getSession() {
        return session;
    }
}
