package com.riverstone.unknown303.modmanager.common.networking.context.base;

import com.riverstone.unknown303.modmanager.common.global.Identifier;

import java.util.UUID;

public interface ServerPacketContext extends PacketContext {
    String clientAddress();

    void createAccount(String username, String password, String deviceId, UUID requestId);
    void login(String username, String password, String deviceId, boolean rememberMe, UUID requestId);

    void createProject(String displayName, UUID requestId, String deviceId, String token);
    void openProject(Identifier projectId, UUID requestId, String deviceId, String token);
    void deleteProject(Identifier projectId, UUID requestId);
    void listProjects(UUID requestId);
}
