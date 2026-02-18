package com.riverstone.unknown303.modmanager.common.networking.packet.custom.server.project;

import com.riverstone.unknown303.modmanager.common.data.NetworkCodec;
import com.riverstone.unknown303.modmanager.common.networking.context.base.ServerPacketContext;
import com.riverstone.unknown303.modmanager.common.networking.packet.ServerboundPacket;

public class ServerboundGetProjectsPacket extends ServerboundPacket<ServerboundGetProjectsPacket> {
    @Override
    public void handle(ServerPacketContext context) {
        context.listProjects(getRequestId());
    }

    @Override
    public NetworkCodec<ServerboundGetProjectsPacket> getCodec() {
        return null;
    }
}
