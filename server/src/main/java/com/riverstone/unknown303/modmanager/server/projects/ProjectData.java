package com.riverstone.unknown303.modmanager.server.projects;

import com.riverstone.unknown303.modmanager.common.project.Project;
import com.riverstone.unknown303.modmanager.common.project.ProjectPermission;
import com.riverstone.unknown303.modmanager.common.project.ProjectRole;

import java.util.Map;
import java.util.UUID;

public final class ProjectData {
    private final Project project;
    private final ProjectMembers members;

    public ProjectData(Project project, UUID ownerId, Map<UUID, ProjectRole> members) {
        this.project = project;
        this.members = new ProjectMembers(ownerId, members);
    }

    public Project getProject() {
        return project;
    }

    public UUID getOwnerId() {
        return members.getOwnerId();
    }

    public ProjectMembers getMembers() {
        return members;
    }

    public ProjectRole getRole(UUID userId) {
        return members.getRole(userId);
    }

    public boolean hasPermission(UUID userId, ProjectPermission permission) {
        return members.hasPermission(userId, permission);
    }
}
