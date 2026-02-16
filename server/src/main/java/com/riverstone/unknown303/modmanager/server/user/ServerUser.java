package com.riverstone.unknown303.modmanager.server.user;

import com.riverstone.unknown303.modmanager.common.user.UserIdentity;

import java.util.UUID;

public final class ServerUser {
    private final UUID id;
    private final String username;
    private final String passwordHash;

    public ServerUser(UUID id, String username, String passwordHash) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public UserIdentity toIdentity() {
        return new UserIdentity(id, username);
    }
}
