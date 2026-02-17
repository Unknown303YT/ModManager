package com.riverstone.unknown303.modmanager.client.networking.context;

import com.riverstone.unknown303.modmanager.common.networking.context.base.ClientPacketContext;

public class ClientPacketHandlerImpl implements ClientPacketContext {
    private final String serverAddress;

    public ClientPacketHandlerImpl(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    @Override
    public String serverAddress() {
        return serverAddress;
    }
}
