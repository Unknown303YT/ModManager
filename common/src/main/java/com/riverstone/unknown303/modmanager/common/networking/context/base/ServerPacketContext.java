package com.riverstone.unknown303.modmanager.common.networking.context.base;

import com.riverstone.unknown303.modmanager.common.networking.packet.status.StatusCode;
import com.riverstone.unknown303.modmanager.common.networking.packet.status.StatusType;

import java.util.Map;
import java.util.UUID;

public interface ServerPacketContext extends PacketContext {
    String clientAddress();

    void createAccount(String username, String password, String deviceId, UUID requestId);
    void login(String username, String password, String deviceId, boolean rememberMe, UUID requestId);

    void sendStatus(StatusType type, StatusCode code, String message,
                    UUID respondingTo, Map<String, String> metadata);
}
