package com.riverstone.unknown303.modmanager.common.networking.packet.custom.client;

import com.riverstone.unknown303.modmanager.common.data.NetworkCodec;
import com.riverstone.unknown303.modmanager.common.networking.context.base.ClientPacketContext;
import com.riverstone.unknown303.modmanager.common.networking.packet.ClientboundPacket;
import com.riverstone.unknown303.modmanager.common.user.UserIdentity;

public class ClientboundLoginSuccessPacket extends ClientboundPacket<ClientboundLoginSuccessPacket> {
    private final UserIdentity identity;
    private final String token;

    public ClientboundLoginSuccessPacket(UserIdentity identity, String token) {
        this.identity = identity;
        this.token = token;
    }

    @Override
    public void handle(ClientPacketContext context) {
        context.onLoginSuccess(identity, token);
    }

    @Override
    public NetworkCodec<ClientboundLoginSuccessPacket> getCodec() {
        return null;
    }

    public UserIdentity getIdentity() {
        return identity;
    }

    public String getToken() {
        return token;
    }
}
