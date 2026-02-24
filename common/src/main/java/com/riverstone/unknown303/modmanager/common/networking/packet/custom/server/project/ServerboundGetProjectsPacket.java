package com.riverstone.unknown303.modmanager.common.networking.packet.custom.server.project;

import com.riverstone.unknown303.modmanager.common.data.NetworkCodec;
import com.riverstone.unknown303.modmanager.common.networking.FriendlyByteBuf;
import com.riverstone.unknown303.modmanager.common.networking.context.base.ServerPacketContext;
import com.riverstone.unknown303.modmanager.common.networking.packet.Packets;
import com.riverstone.unknown303.modmanager.common.networking.packet.ServerboundPacket;

public final class ServerboundGetProjectsPacket extends ServerboundPacket<ServerboundGetProjectsPacket> {
    public void encode(FriendlyByteBuf buf) {}

    public static ServerboundGetProjectsPacket decode(FriendlyByteBuf buf) {
        return new ServerboundGetProjectsPacket();
    }

    @Override
    public void handle(ServerPacketContext context) {
        context.listProjects(getRequestId());
    }

    @Override
    public NetworkCodec<ServerboundGetProjectsPacket> getCodec() {
        return Packets.SERVERBOUND_GET_PROJECTS_PACKET;
    }
}
