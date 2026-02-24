package com.riverstone.unknown303.modmanager.server.projects;

import com.riverstone.unknown303.modmanager.common.project.ProjectPermission;
import com.riverstone.unknown303.modmanager.common.project.ProjectRole;

import java.util.Map;
import java.util.UUID;

public final class ProjectMembers {
    private final UUID ownerId;
    private final Map<UUID, ProjectRole> members;

    public ProjectMembers(UUID ownerId, Map<UUID, ProjectRole> members) {
        this.ownerId = ownerId;
        this.members = members;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public Map<UUID, ProjectRole> getMembers() {
        return members;
    }

    public ProjectRole getRole(UUID userId) {
        if (ownerId.equals(userId))
            return ProjectRole.OWNER;
        return members.get(userId);
    }

    public boolean hasPermission(UUID userId, ProjectPermission permission) {
        ProjectRole role = getRole(userId);
        return role != null && role.has(permission);
    }
}
