package com.riverstone.unknown303.modmanager.client.user;

import com.riverstone.unknown303.modmanager.common.user.UserIdentity;

public final class ClientUser {
    private final UserIdentity identity;

    public ClientUser(UserIdentity identity) {
        this.identity = identity;
    }

    public UserIdentity getIdentity() {
        return identity;
    }
}
