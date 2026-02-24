package com.riverstone.unknown303.modmanager.server.networking.context;

import com.riverstone.unknown303.modmanager.common.global.Identifier;
import com.riverstone.unknown303.modmanager.common.networking.context.base.ServerPacketContext;
import com.riverstone.unknown303.modmanager.server.auth.AuthService;
import com.riverstone.unknown303.modmanager.server.projects.ProjectService;

import java.util.UUID;

public class ServerPacketContextImpl implements ServerPacketContext {
    private final String clientAddress;

    public ServerPacketContextImpl(String clientAddress) {
        this.clientAddress = clientAddress;
    }

    @Override
    public String clientAddress() {
        return clientAddress;
    }

    @Override
    public void createAccount(String username, String password, String deviceId, UUID requestId) {
        AuthService.INSTANCE.createAccount(clientAddress(), username, password, deviceId, requestId);
    }

    @Override
    public void login(String username, String password, String deviceId, boolean rememberMe, UUID requestId) {
        AuthService.INSTANCE.login(clientAddress(), username, password, deviceId, rememberMe, requestId);
    }

    @Override
    public void createProject(String displayName, UUID requestId, String deviceId, String token) {
        ProjectService.INSTANCE.createProject(clientAddress(), displayName, requestId, deviceId, token);
    }

    @Override
    public void openProject(Identifier projectId, UUID requestId, String deviceId, String token) {
        ProjectService.INSTANCE.openProject(this, clientAddress(),
                projectId, requestId, deviceId, token);
    }

    @Override
    public void deleteProject(Identifier projectId, UUID requestId) {
        ProjectService.INSTANCE.deleteProject(clientAddress(), projectId, requestId);
    }

    @Override
    public void listProjects(UUID requestId) {
        ProjectService.INSTANCE.listProjects(clientAddress(), requestId);
    }
}
