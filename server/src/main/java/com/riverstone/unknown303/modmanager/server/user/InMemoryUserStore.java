package com.riverstone.unknown303.modmanager.server.user;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryUserStore implements UserStore {
    private final Map<UUID, ServerUser> byId = new HashMap<>();
    private final Map<String, ServerUser> byUsername = new HashMap<>();

    @Override
    public ServerUser getByUsername(String username) {
        return byUsername.get(username);
    }

    @Override
    public ServerUser getById(UUID id) {
        return byId.get(id);
    }

    @Override
    public void save(ServerUser user) {
        byId.put(user.getId(), user);
        byUsername.put(user.getUsername().toLowerCase(), user);
    }

    @Override
    public Collection<ServerUser> all() {
        return byId.values();
    }
}
