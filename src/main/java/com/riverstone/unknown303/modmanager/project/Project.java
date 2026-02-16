package com.riverstone.unknown303.modmanager.project;

import com.riverstone.unknown303.modmanager.data.NetworkCodec;
import com.riverstone.unknown303.modmanager.data.SaveCodec;
import com.riverstone.unknown303.modmanager.global.Identifier;
import com.riverstone.unknown303.modmanager.networking.FriendlyByteBuf;
import com.riverstone.unknown303.modmanager.user.User;
import com.riverstone.unknown303.modmanager.user.Users;

import java.util.*;

public class Project {
    public static final NetworkCodec<Map.Entry<Identifier, ProjectRole>> NETWORK_CODEC =
            NetworkCodec.toDatabaseBuilder(NetworkCodec.builder(ProjectRole.class)
                    .encoder((role, buf) ->
                            buf.writeVarInt(role.ordinal()))
                    .decoder(buf -> ProjectRole.values()[buf.readVarInt()]));
    public static final SaveCodec<Map.Entry<Identifier, ProjectRole>> SAVE_CODEC =
            SaveCodec.toDatabaseBuilder(SaveCodec.builder(ProjectRole.class)
                    .encoder((role, json) ->
                            json.addProperty("role", role.ordinal()))
                    .decoder(json ->
                            ProjectRole.values()[json.get("role").getAsInt()]));

    private final Identifier id;
    private String name;

    private final Map<Identifier, ProjectRole> members = new HashMap<>();

    public Project(Identifier id, String name) {
        this.id = id;
        this.name = name;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(id.toString());
        buf.writeUtf(name);

    }

    public Identifier getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void rename(String newName) {
        this.name = newName;
    }

    public void addMember(User user, ProjectRole role) {
        members.put(Users.getId(user), role);
    }

    public void removeMembers(User user) {
        members.remove(Users.getId(user));
    }

    public boolean hasMember(User user) {
        return members.containsKey(user);
    }

    public ProjectRole getRole(User user) {
        return members.get(user);
    }

    public boolean hasRole(User user, ProjectRole role) {
        ProjectRole userRole = members.get(user);
        return userRole != null && userRole == role;
    }

    public Map<Identifier, ProjectRole> getMembers() {
        return Collections.unmodifiableMap(members);
    }

    public boolean hasPermission(User user, ProjectPermission permission) {
        ProjectRole role = members.get(user);
        return role != null && role.has(permission);
    }

    public boolean isOwner(User user) {
        return hasRole(user, ProjectRole.OWNER);
    }

    public List<Map.Entry<User, ProjectRole>> getMemberEntries() {
        List<Map.Entry<User, ProjectRole>> list = new ArrayList<>();
        for (Map.Entry<Identifier, ProjectRole> entry : members.entrySet())
            list.add(Map.entry(Users.getUser(entry.getKey()), entry.getValue()));
        return list;
    }

    public void loadMemberEntries(List<Map.Entry<User, ProjectRole>> entries) {
        members.clear();
        for (Map.Entry<User, ProjectRole> entry : entries)
            members.put(Users.getId(entry.getKey()), entry.getValue());
    }
}
