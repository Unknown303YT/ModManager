package com.riverstone.unknown303.modmanager.server.user;

import java.util.Collection;
import java.util.UUID;

public interface UserStore {
    ServerUser getByUsername(String username);
    ServerUser getById(UUID id);
    void save(ServerUser user);
    Collection<ServerUser> all();
}
