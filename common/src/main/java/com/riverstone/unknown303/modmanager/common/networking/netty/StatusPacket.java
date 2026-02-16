package com.riverstone.unknown303.modmanager.common.networking.netty;

import com.riverstone.unknown303.modmanager.common.networking.packet.ClientboundPacket;
import com.riverstone.unknown303.modmanager.common.data.NetworkCodec;
import com.riverstone.unknown303.modmanager.common.networking.FriendlyByteBuf;

public class StatusPacket extends ClientboundPacket<StatusPacket> {
    private final String message;

    public StatusPacket(String message) {
        this.message = message;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(message);
    }

    public static StatusPacket decode(FriendlyByteBuf buf) {
        return new StatusPacket(buf.readUtf());
    }

    @Override
    public NetworkCodec<StatusPacket> getCodec() {
        return null;
    }

    @Override
    public String handle(String clientAddress) {
        return message;
    }
}
