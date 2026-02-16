package com.riverstone.unknown303.modmanager.common.user;

import java.util.UUID;

public final class UserIdentity {
    private final UUID id;
    private final String username;

    public UserIdentity(UUID id, String username) {
        this.id = id;
        this.username = username;
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
}
