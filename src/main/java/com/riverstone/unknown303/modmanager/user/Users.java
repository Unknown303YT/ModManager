package com.riverstone.unknown303.modmanager.user;

import com.riverstone.unknown303.modmanager.data.NetworkCodec;
import com.riverstone.unknown303.modmanager.data.Database;
import com.riverstone.unknown303.modmanager.data.SaveCodec;
import com.riverstone.unknown303.modmanager.global.Identifier;

import java.util.Map;

public class Users {
    public static final NetworkCodec<User> NETWORK_CODEC =
            NetworkCodec.builder(User.class).encoder(
                    (user, buf) -> {
                        buf.writeUtf(user.getUsername());
                        buf.writeBoolean(user.isServerSide());
                        buf.writeUtf(user.getToken());
                        if (user.isServerSide()) {
                            buf.writeUtf(user.getHash());
                            buf.writeUtf(user.getSalt());
                        }
                    }
            ).decoder(networker -> {
                String username = networker.readUtf();
                boolean isServerSide = networker.readBoolean();
                String token = networker.readUtf();
                String hash = "";
                String salt = "";
                if (isServerSide) {
                    hash = networker.readUtf();
                    salt = networker.readUtf();
                }

                return new User(username, hash, salt, token, isServerSide);
            });
    public static final SaveCodec<User> SAVE_CODEC =
            SaveCodec.builder(User.class).encoder(
                    (user, json) -> {
                        json.addProperty("username", user.getUsername());
                        json.addProperty("isServerSide", user.isServerSide());
                        json.addProperty("token", user.getToken());
                        if (user.isServerSide()) {
                            json.addProperty("hash", user.getHash());
                            json.addProperty("salt", user.getSalt());
                        }
                    }
            ).decoder(json -> {
                String username = json.get("username").getAsString();
                boolean isServerSide = json.get("isServerSide").getAsBoolean();
                String token = json.get("token").getAsString();
                String hash = "";
                String salt = "";
                if (isServerSide) {
                    hash = json.get("hash").getAsString();
                    salt = json.get("salt").getAsString();
                }

                return new User(username, hash, salt, token, isServerSide);
            });

    public static final SaveCodec<Map.Entry<Identifier, User>> USER_DATABASE_BUILDER =
            SaveCodec.toDatabaseBuilder(SAVE_CODEC);

    public static final Database<Map.Entry<Identifier, User>> USERS =
            new Database<>(USER_DATABASE_BUILDER);

    public static final User SYSTEM = new User(
            "system", "", "", "", true);

    public static User getUser(Identifier id) {
        for (Map.Entry<Identifier, User> entry : USERS.getData()) {
            if (entry.getKey() == id)
                return entry.getValue();
        }

        return null;
    }

    public static Identifier getId(User user) {
        for (Map.Entry<Identifier, User> entry : USERS.getData()) {
            if (entry.getValue() == user)
                return entry.getKey();
        }

        return null;
    }

    public static void register() {
        USERS.add(Map.entry(new Identifier(SYSTEM, "system_admin"), SYSTEM));
    }
}
