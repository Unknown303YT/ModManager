package com.riverstone.unknown303.modmanager.common.networking.packet.custom.server.project;

import com.riverstone.unknown303.modmanager.common.data.NetworkCodec;
import com.riverstone.unknown303.modmanager.common.global.Identifier;
import com.riverstone.unknown303.modmanager.common.networking.FriendlyByteBuf;
import com.riverstone.unknown303.modmanager.common.networking.context.base.ServerPacketContext;
import com.riverstone.unknown303.modmanager.common.networking.packet.AuthenticatedPacket;
import com.riverstone.unknown303.modmanager.common.networking.packet.Packets;

public final class ServerboundOpenProjectPacket extends AuthenticatedPacket<ServerboundOpenProjectPacket> {
    private final Identifier projectId;

    public ServerboundOpenProjectPacket(Identifier projectId) {
        this.projectId = projectId;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeIdentifier(projectId);
    }

    public static ServerboundOpenProjectPacket decode(FriendlyByteBuf buf) {
        return new ServerboundOpenProjectPacket(buf.readIdentifier());
    }

    @Override
    public void handle(ServerPacketContext context) {
        context.openProject(projectId, getRequestId(), deviceId, token);
    }

    @Override
    public NetworkCodec<ServerboundOpenProjectPacket> getCodec() {
        return Packets.SERVERBOUND_OPEN_PROJECT_PACKET;
    }
}
