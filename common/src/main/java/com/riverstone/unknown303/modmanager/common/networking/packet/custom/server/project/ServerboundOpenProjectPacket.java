package com.riverstone.unknown303.modmanager.common.networking.packet.custom.server.project;

import com.riverstone.unknown303.modmanager.common.data.NetworkCodec;
import com.riverstone.unknown303.modmanager.common.global.Identifier;
import com.riverstone.unknown303.modmanager.common.networking.context.base.ServerPacketContext;
import com.riverstone.unknown303.modmanager.common.networking.packet.ServerboundPacket;

public final class ServerboundOpenProjectPacket extends ServerboundPacket<ServerboundOpenProjectPacket> {
    private final Identifier projectId;

    public ServerboundOpenProjectPacket(Identifier projectId) {
        this.projectId = projectId;
    }

    @Override
    public void handle(ServerPacketContext context) {
        context.openProject(projectId, getRequestId());
    }

    @Override
    public NetworkCodec<ServerboundOpenProjectPacket> getCodec() {
        return null;
    }
}
