package com.riverstone.unknown303.modmanager.common.networking.packet.custom.server;

import com.riverstone.unknown303.modmanager.common.data.NetworkCodec;
import com.riverstone.unknown303.modmanager.common.networking.FriendlyByteBuf;
import com.riverstone.unknown303.modmanager.common.networking.context.base.ServerPacketContext;
import com.riverstone.unknown303.modmanager.common.networking.packet.Packets;
import com.riverstone.unknown303.modmanager.common.networking.packet.ServerboundPacket;

public final class ServerboundLoginPacket extends ServerboundPacket<ServerboundLoginPacket> {
    private final String username;
    private final String password;
    private final String deviceId;
    private final boolean rememberMe;

    public ServerboundLoginPacket(String username, String password, String deviceId, boolean rememberMe) {
        this.username = username;
        this.password = password;
        this.deviceId = deviceId;
        this.rememberMe = rememberMe;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(username);
        buf.writeUtf(password);
        buf.writeUtf(deviceId);
        buf.writeBoolean(rememberMe);
    }

    public static ServerboundLoginPacket decode(FriendlyByteBuf buf) {
        return new ServerboundLoginPacket(buf.readUtf(), buf.readUtf(), buf.readUtf(), buf.readBoolean());
    }

    @Override
    public void handle(ServerPacketContext context) {
        context.login(username, password, deviceId, rememberMe, getRequestId());
    }

    @Override
    public NetworkCodec<ServerboundLoginPacket> getCodec() {
        return Packets.SERVERBOUND_LOGIN_PACKET;
    }
}
