package com.riverstone.unknown303.modmanager.common.global;

import com.riverstone.unknown303.modmanager.common.user.UserIdentity;
import com.riverstone.unknown303.modmanager.common.user.Users;

import java.util.Map;
import java.util.Objects;

public record Identifier(UserIdentity user, String path) {
    public static Identifier parse(String id) {
        String[] elements = id.split(":");
        if (elements.length != 2)
            Logger.getLogger().fatal("Given Identifier string has multiple : " +
                            "in it! Expected [username]:[path], got " + id,
                    IllegalArgumentException::new);
        UserIdentity user = null;
        for (Map.Entry<Identifier, UserIdentity> entry : Users.REGISTRY.getEntries()) {
            if (Objects.equals(entry.getValue().getUsername(), elements[0])) {
                user = entry.getValue();
                break;
            }
        }
        if (user == null)
            Logger.getLogger().fatal("Error: Attempting to use an unregistered " +
                            "User! User's username: " + elements[0],
                    IllegalArgumentException::new);

        return new Identifier(user, elements[1]);
    }

    public String toFileSafeString() {
        String safeUser = user.getId().toString().replaceAll("[^a-zA-Z0-9._-]", "_");
        String safePath = path.replaceAll("[^a-zA-Z0-9._-]", "_");

        String hash = Integer.toHexString(this.toString().hashCode());

        return "%s_%s_%s".formatted(safeUser, safePath, hash);
    }

    @Override
    public String toString() {
        return user.getId() + ":" + path;
    }
}
