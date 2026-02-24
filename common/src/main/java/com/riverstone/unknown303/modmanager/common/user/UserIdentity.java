package com.riverstone.unknown303.modmanager.common.user;

import com.riverstone.unknown303.modmanager.common.data.NetworkCodec;

import java.util.UUID;

public final class UserIdentity {
    public static final NetworkCodec<UserIdentity> NETWORK_CODEC =
            NetworkCodec.builder(UserIdentity.class)
                    .encoder((user, buf) -> {
                        buf.writeUUID(user.getId());
                        buf.writeUtf(user.getUsername());
                    })
                    .decoder(buf ->
                            new UserIdentity(buf.readUUID(), buf.readUtf()));

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
