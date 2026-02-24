package com.riverstone.unknown303.modmanager.common.networking.packet.custom.server.project;

import com.riverstone.unknown303.modmanager.common.data.NetworkCodec;
import com.riverstone.unknown303.modmanager.common.networking.FriendlyByteBuf;
import com.riverstone.unknown303.modmanager.common.networking.context.base.ServerPacketContext;
import com.riverstone.unknown303.modmanager.common.networking.packet.AuthenticatedPacket;
import com.riverstone.unknown303.modmanager.common.networking.packet.Packets;

public final class ServerboundCreateProjectPacket extends AuthenticatedPacket<ServerboundCreateProjectPacket> {
    private final String displayName;

    public ServerboundCreateProjectPacket(String displayName) {
        this.displayName = displayName;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(displayName);
    }

    public static ServerboundCreateProjectPacket decode(FriendlyByteBuf buf) {
        return new ServerboundCreateProjectPacket(buf.readUtf());
    }

    @Override
    public void handle(ServerPacketContext context) {
        context.createProject(displayName, getRequestId());
    }

    @Override
    public NetworkCodec<ServerboundCreateProjectPacket> getCodec() {
        return Packets.SERVERBOUND_CREATE_PROJECT_PACKET;
    }
}
