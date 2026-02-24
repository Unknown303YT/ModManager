package com.riverstone.unknown303.modmanager.server.projects;

import com.riverstone.unknown303.modmanager.common.global.Identifier;
import com.riverstone.unknown303.modmanager.common.networking.context.base.ServerPacketContext;
import com.riverstone.unknown303.modmanager.common.networking.packet.custom.client.ClientboundProjectListPacket;
import com.riverstone.unknown303.modmanager.common.networking.packet.custom.client.ClientboundProjectOpenedPacket;
import com.riverstone.unknown303.modmanager.common.networking.packet.status.ClientboundStatusPacket;
import com.riverstone.unknown303.modmanager.common.networking.packet.status.StatusCode;
import com.riverstone.unknown303.modmanager.common.networking.packet.status.StatusType;
import com.riverstone.unknown303.modmanager.common.project.Project;
import com.riverstone.unknown303.modmanager.common.project.ProjectPermission;
import com.riverstone.unknown303.modmanager.common.user.UserIdentity;
import com.riverstone.unknown303.modmanager.common.user.Users;
import com.riverstone.unknown303.modmanager.server.Server;
import com.riverstone.unknown303.modmanager.server.auth.AuthService;
import com.riverstone.unknown303.modmanager.server.saving.SaveManagerServer;

import java.util.*;

public final class ProjectService {
    public static final ProjectService INSTANCE = new ProjectService();

    private final ProjectStore store = new ProjectStore();
    private final SaveManagerServer saveManager = new SaveManagerServer();

    private ProjectService() {}

    public void createProject(String client, String displayName, UUID requestId, String deviceId, String token) {
        UUID userId = AuthService.INSTANCE.authenticate(token, deviceId, client);
        if (userId == null) {
            Server.sendPacket(client, new ClientboundStatusPacket(
                    StatusType.ERROR,
                    StatusCode.PERMISSION_DENIED,
                    "Token invalid!",
                    requestId,
                    Map.of("sessionToken", token)
            ));
            return;
        }

        UserIdentity owner = null;

        for (UserIdentity user : Users.REGISTRY) {
            if (user.getId() == userId) {
                owner = user;
                break;
            }
        }

        if (owner == null) {
            Server.sendPacket(client, new ClientboundStatusPacket(
                    StatusType.ERROR,
                    StatusCode.INVALID_INPUT,
                    "User not found!",
                    requestId,
                    Map.of("userId", userId.toString())
            ));
            return;
        }

        ProjectData data = store.create(owner, displayName);

        Server.sendPacket(client, new ClientboundProjectOpenedPacket(data.getProject()));
        Server.sendPacket(client, new ClientboundStatusPacket(
                StatusType.SUCCESS,
                StatusCode.PROJECT_CREATED,
                "Project created!",
                requestId,
                Map.of("projectId", data.getProject().getId().toString())
        ));
    }

    public void openProject(ServerPacketContext context, String client, Identifier id, UUID requestId, String deviceId, String token) {
        ProjectData data = store.get(id);

        if (data == null) {
            Server.sendPacket(client, new ClientboundStatusPacket(
                    StatusType.ERROR,
                    StatusCode.PROJECT_NOT_FOUND,
                    "Project not found!",
                    requestId,
                    Map.of("projectId", id.toString())
            ));
            return;
        }

        UUID userId = AuthService.INSTANCE.authenticate(token, deviceId, client);

        if (userId == null) {
            Server.sendPacket(client, new ClientboundStatusPacket(
                    StatusType.ERROR,
                    StatusCode.PERMISSION_DENIED,
                    "Token invalid!",
                    requestId,
                    Map.of("sessionToken", token)
            ));
            return;
        }

        if (!data.hasPermission(userId, ProjectPermission.VIEW_CONTENT)) {
            Server.sendPacket(client, new ClientboundStatusPacket(
                    StatusType.ERROR,
                    StatusCode.PERMISSION_DENIED,
                    "You do not have permission to view this project!",
                    requestId,
                    Map.of("projectId", id.toString())));
            return;
        }

        saveManager.loadProjectData(id);

        Server.sendPacket(client, new ClientboundProjectOpenedPacket(data.getProject()));
        Server.sendPacket(client, new ClientboundStatusPacket(
                StatusType.SUCCESS,
                StatusCode.PROJECT_OPENED,
                "Project opened",
                requestId,
                Map.of()
        ));
    }

    public void deleteProject(String client, Identifier id, UUID requestId) {
        if (!store.delete(id)) {
            Server.sendPacket(client, new ClientboundStatusPacket(
                    StatusType.ERROR,
                    StatusCode.PROJECT_NOT_FOUND,
                    "Project not found",
                    requestId,
                    Map.of("projectId", id.toString())
            ));
            Server.sendPacket(client, new ClientboundStatusPacket(
                    StatusType.ERROR,
                    StatusCode.PROJECT_DELETE_FAILED,
                    "Project deletion failed",
                    requestId,
                    Map.of("projectId", id.toString())
            ));
            return;
        }

        Server.sendPacket(client, new ClientboundStatusPacket(
                StatusType.SUCCESS,
                StatusCode.PROJECT_DELETED,
                "Project deleted",
                requestId,
                Map.of("projectId", id.toString())
        ));
    }

    public void listProjects(String client, UUID requestId) {
        List<Project> list = new ArrayList<>(store.all().stream().map(ProjectData::getProject).toList());

        Server.sendPacket(client, new ClientboundProjectListPacket(list));
        Server.sendPacket(client, new ClientboundStatusPacket(
                StatusType.SUCCESS,
                StatusCode.PROJECT_LISTED,
                "Projects listed",
                requestId,
                Map.of("count", String.valueOf(list.size()))
        ));
    }
}
