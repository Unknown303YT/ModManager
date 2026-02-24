package com.riverstone.unknown303.modmanager.common.networking.packet.custom.server;

import com.riverstone.unknown303.modmanager.common.data.NetworkCodec;
import com.riverstone.unknown303.modmanager.common.networking.FriendlyByteBuf;
import com.riverstone.unknown303.modmanager.common.networking.context.base.ServerPacketContext;
import com.riverstone.unknown303.modmanager.common.networking.packet.Packets;
import com.riverstone.unknown303.modmanager.common.networking.packet.ServerboundPacket;

public final class ServerboundCreateAccountPacket extends ServerboundPacket<ServerboundCreateAccountPacket> {
    private final String username;
    private final String password;
    private final String deviceId;

    public ServerboundCreateAccountPacket(String username, String password, String deviceId) {
        this.username = username;
        this.password = password;
        this.deviceId = deviceId;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(username);
        buf.writeUtf(password);
        buf.writeUtf(deviceId);
    }

    public static ServerboundCreateAccountPacket decode(FriendlyByteBuf buf) {
        return new ServerboundCreateAccountPacket(buf.readUtf(), buf.readUtf(), buf.readUtf());
    }

    @Override
    public NetworkCodec<ServerboundCreateAccountPacket> getCodec() {
        return Packets.SERVERBOUND_CREATE_ACCOUNT_PACKET;
    }

    @Override
    public void handle(ServerPacketContext context) {
        context.createAccount(username, password, deviceId, getRequestId());
    }
}
