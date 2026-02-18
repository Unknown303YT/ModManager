package com.riverstone.unknown303.modmanager.common.networking.packet.custom.server.project;

import com.riverstone.unknown303.modmanager.common.data.NetworkCodec;
import com.riverstone.unknown303.modmanager.common.networking.context.base.ServerPacketContext;
import com.riverstone.unknown303.modmanager.common.networking.packet.ServerboundPacket;

public class ServerboundCreateProjectPacket extends ServerboundPacket<ServerboundCreateProjectPacket> {
    private final String displayName;

    public ServerboundCreateProjectPacket(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public void handle(ServerPacketContext context) {
        context.createProject(displayName, getRequestId());
    }

    @Override
    public NetworkCodec<ServerboundCreateProjectPacket> getCodec() {
        return null;
    }
}
