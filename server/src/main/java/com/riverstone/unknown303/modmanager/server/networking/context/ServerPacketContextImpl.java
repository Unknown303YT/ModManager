package com.riverstone.unknown303.modmanager.server.networking.context;

import com.riverstone.unknown303.modmanager.common.networking.context.base.ServerPacketContext;
import com.riverstone.unknown303.modmanager.common.networking.packet.status.ClientboundStatusPacket;
import com.riverstone.unknown303.modmanager.common.networking.packet.status.StatusCode;
import com.riverstone.unknown303.modmanager.common.networking.packet.status.StatusType;
import com.riverstone.unknown303.modmanager.server.Server;
import com.riverstone.unknown303.modmanager.server.auth.AuthService;

import java.util.Map;
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
        AuthService.INSTANCE.createAccount(clientAddress, username, password, deviceId, requestId);
    }

    @Override
    public void login(String username, String password, String deviceId, boolean rememberMe, UUID requestId) {
        AuthService.INSTANCE.login(clientAddress, username, password, deviceId, rememberMe, requestId);
    }

    @Override
    public void sendStatus(StatusType type, StatusCode code, String message, UUID respondingTo, Map<String, String> metadata) {
        Server.sendPacket(clientAddress, new ClientboundStatusPacket(type, code, message, respondingTo, metadata));
    }
}
