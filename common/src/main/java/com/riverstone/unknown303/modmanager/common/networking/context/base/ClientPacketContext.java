package com.riverstone.unknown303.modmanager.common.networking.context.base;

import com.riverstone.unknown303.modmanager.common.networking.packet.status.ClientboundStatusPacket;
import com.riverstone.unknown303.modmanager.common.user.UserIdentity;

public interface ClientPacketContext extends PacketContext {
    String serverAddress();

    void onStatus(ClientboundStatusPacket status);
    void onLoginSuccess(UserIdentity identity, String token);
}
