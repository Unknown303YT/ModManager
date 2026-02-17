package com.riverstone.unknown303.modmanager.common.networking.packet;

import com.riverstone.unknown303.modmanager.common.networking.context.base.ClientPacketContext;
import com.riverstone.unknown303.modmanager.common.networking.context.base.PacketContext;

public abstract class ClientboundPacket<SELF extends ClientboundPacket<SELF>>
        extends Packet<SELF> {
    public ClientboundPacket() {
        super(false);
    }

    @Override
    public final void runPacket(PacketContext context) {
        if (!(context instanceof ClientPacketContext clientContext))
            return;
        handle(clientContext);
    }

    public abstract void handle(ClientPacketContext context);
}
