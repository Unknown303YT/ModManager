package com.riverstone.unknown303.modmanager.project;

import com.riverstone.unknown303.modmanager.data.SaveCodec;

import java.util.EnumSet;

public enum ProjectRole {
    OWNER(EnumSet.of(
            ProjectPermission.MANAGE_PROJECT,
            ProjectPermission.MANAGE_MEMBERS,
            ProjectPermission.EDIT_CONTENT,
            ProjectPermission.DELETE_CONTENT,
            ProjectPermission.VIEW_CONTENT
    )),
    ADMIN(EnumSet.of(
            ProjectPermission.MANAGE_MEMBERS,
            ProjectPermission.EDIT_CONTENT,
            ProjectPermission.DELETE_CONTENT,
            ProjectPermission.VIEW_CONTENT
    )),
    EDITOR(EnumSet.of(
            ProjectPermission.EDIT_CONTENT,
            ProjectPermission.VIEW_CONTENT
    )),
    VIEWER(EnumSet.of(
            ProjectPermission.VIEW_CONTENT
    ));

    private final EnumSet<ProjectPermission> permissions;

    ProjectRole(EnumSet<ProjectPermission> permissions) {
        this.permissions = permissions;
    }

    public boolean has(ProjectPermission permission) {
        return permissions.contains(permission);
    }
}
