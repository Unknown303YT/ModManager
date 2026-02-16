package com.riverstone.unknown303.modmanager.global;

import com.riverstone.unknown303.modmanager.data.Registries;
import com.riverstone.unknown303.modmanager.data.Registry;

import java.util.Map;
import java.util.Objects;

public record Identifier(User user, String path) {
    public static Identifier parse(String id) {
        String[] elements = id.split(":");
        if (elements.length != 2)
            Logger.getLogger().fatal("Given Identifier string has multiple : " +
                            "in it! Expected [username]:[path], got " + id,
                    IllegalArgumentException::new);
        User user = null;
        for (Map.Entry<Identifier, User> entry : Users.USERS.getData()) {
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

    @Override
    public String toString() {
        return user.getUsername() + ":" + path;
    }
}
