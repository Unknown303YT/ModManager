package com.riverstone.unknown303.modmanager.client.networking.context;

import com.riverstone.unknown303.modmanager.common.networking.context.base.ClientPacketContext;
import com.riverstone.unknown303.modmanager.common.networking.packet.status.ClientboundStatusPacket;
import com.riverstone.unknown303.modmanager.common.user.UserIdentity;

public class ClientPacketHandlerImpl implements ClientPacketContext {
    private final String serverAddress;

    public ClientPacketHandlerImpl(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    @Override
    public String serverAddress() {
        return serverAddress;
    }

    @Override
    public void onStatus(ClientboundStatusPacket status) {
        // STATUS
    }

    @Override
    public void onLoginSuccess(UserIdentity identity, String token) {
        AuthClient.INSTANCE.onLoginSuccess(identity, token);
    }
}
