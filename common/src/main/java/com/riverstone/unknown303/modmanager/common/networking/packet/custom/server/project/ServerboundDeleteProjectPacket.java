package com.riverstone.unknown303.modmanager.common.networking.packet.custom.server.project;

import com.riverstone.unknown303.modmanager.common.data.NetworkCodec;
import com.riverstone.unknown303.modmanager.common.global.Identifier;
import com.riverstone.unknown303.modmanager.common.networking.FriendlyByteBuf;
import com.riverstone.unknown303.modmanager.common.networking.context.base.ServerPacketContext;
import com.riverstone.unknown303.modmanager.common.networking.packet.AuthenticatedPacket;
import com.riverstone.unknown303.modmanager.common.networking.packet.Packets;

public final class ServerboundDeleteProjectPacket extends AuthenticatedPacket<ServerboundDeleteProjectPacket> {
    private final Identifier projectId;

    public ServerboundDeleteProjectPacket(Identifier projectId) {
        this.projectId = projectId;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeIdentifier(projectId);
    }

    public static ServerboundDeleteProjectPacket decode(FriendlyByteBuf buf) {
        return new ServerboundDeleteProjectPacket(buf.readIdentifier());
    }

    @Override
    public void handle(ServerPacketContext context) {
        context.deleteProject(projectId, getRequestId());
    }

    @Override
    public NetworkCodec<ServerboundDeleteProjectPacket> getCodec() {
        return Packets.SERVERBOUND_DELETE_PROJECT_PACKET;
    }
}
