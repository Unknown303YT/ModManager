package com.riverstone.unknown303.modmanager.global;

import com.riverstone.unknown303.modmanager.data.DataBuilder;
import com.riverstone.unknown303.modmanager.data.Database;

import java.util.Map;

public class Users {
    public static final DataBuilder<User> USER_BUILDER =
            DataBuilder.builder(User.class).encoder(
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

    public static final DataBuilder<Map.Entry<Identifier, User>> USER_DATABASE_BUILDER =
            DataBuilder.toDatabaseBuilder(USER_BUILDER);

    public static final Database<Map.Entry<Identifier, User>> USERS =
            new Database<>(USER_DATABASE_BUILDER);

    public static final User SYSTEM = new User(
            "system", "", "", "", true);

    public static void register() {
        USERS.add(Map.entry(new Identifier(SYSTEM, "system_admin"), SYSTEM));
    }
}
