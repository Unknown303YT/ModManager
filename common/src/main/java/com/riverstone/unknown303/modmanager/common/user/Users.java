package com.riverstone.unknown303.modmanager.common.user;

import com.riverstone.unknown303.modmanager.common.data.NetworkCodec;
import com.riverstone.unknown303.modmanager.common.data.SaveCodec;
import com.riverstone.unknown303.modmanager.common.global.Identifier;
import com.riverstone.unknown303.modmanager.common.registry.Registry;

import java.util.UUID;

public class Users {
    public static final UserIdentity SYSTEM = new UserIdentity(
            UUID.fromString("929f20f9-0e57-4fbe-94c2-e821fb92bb9e"),
            "SYSTEM");

    public static final Registry<UserIdentity> REGISTRY = new Registry<>(
            new Identifier(SYSTEM, "user_identities"),
            SaveCodec.builder(UserIdentity.class)
                    .encoder((identity, json) -> {
                        json.addProperty("id", identity.getId().toString());
                        json.addProperty("username", identity.getUsername());
                    })
                    .decoder(json -> {
                        UUID id = UUID.fromString(json.get("id").getAsString());
                        String username = json.get("username").getAsString();
                        return new UserIdentity(id, username);
                    }),
            NetworkCodec.builder(UserIdentity.class)
    );
}
