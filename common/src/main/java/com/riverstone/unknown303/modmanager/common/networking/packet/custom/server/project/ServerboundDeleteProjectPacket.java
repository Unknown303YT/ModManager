package com.riverstone.unknown303.modmanager.common.networking.packet.custom.server.project;

import com.riverstone.unknown303.modmanager.common.data.NetworkCodec;
import com.riverstone.unknown303.modmanager.common.global.Identifier;
import com.riverstone.unknown303.modmanager.common.networking.context.base.ServerPacketContext;
import com.riverstone.unknown303.modmanager.common.networking.packet.ServerboundPacket;

public class ServerboundDeleteProjectPacket extends ServerboundPacket<ServerboundDeleteProjectPacket> {
    private final Identifier projectId;

    public ServerboundDeleteProjectPacket(Identifier projectId) {
        this.projectId = projectId;
    }

    @Override
    public void handle(ServerPacketContext context) {
        context.deleteProject(context, getRequestId());
    }

    @Override
    public NetworkCodec<ServerboundDeleteProjectPacket> getCodec() {
        return null;
    }
}
