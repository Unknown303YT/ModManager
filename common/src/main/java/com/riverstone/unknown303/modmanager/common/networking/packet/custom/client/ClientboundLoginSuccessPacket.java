package com.riverstone.unknown303.modmanager.common.networking.packet.custom.client;

import com.riverstone.unknown303.modmanager.common.data.NetworkCodec;
import com.riverstone.unknown303.modmanager.common.networking.FriendlyByteBuf;
import com.riverstone.unknown303.modmanager.common.networking.context.base.ClientPacketContext;
import com.riverstone.unknown303.modmanager.common.networking.packet.ClientboundPacket;
import com.riverstone.unknown303.modmanager.common.networking.packet.Packets;
import com.riverstone.unknown303.modmanager.common.user.UserIdentity;
import io.netty.buffer.Unpooled;

public final class ClientboundLoginSuccessPacket extends ClientboundPacket<ClientboundLoginSuccessPacket> {
    private final UserIdentity identity;
    private final String token;

    public ClientboundLoginSuccessPacket(UserIdentity identity, String token) {
        this.identity = identity;
        this.token = token;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBytes(UserIdentity.NETWORK_CODEC.build(identity));
        buf.writeUtf(token);
    }

    public static ClientboundLoginSuccessPacket decode(FriendlyByteBuf buf) {
        return new ClientboundLoginSuccessPacket(
                UserIdentity.NETWORK_CODEC.build(new FriendlyByteBuf(buf.readBytes(Unpooled.buffer()))),
                buf.readUtf());
    }

    @Override
    public void handle(ClientPacketContext context) {
        context.onLoginSuccess(identity, token);
    }

    @Override
    public NetworkCodec<ClientboundLoginSuccessPacket> getCodec() {
        return Packets.CLIENTBOUND_LOGIN_SUCCESS_PACKET;
    }

    public UserIdentity getIdentity() {
        return identity;
    }

    public String getToken() {
        return token;
    }
}
