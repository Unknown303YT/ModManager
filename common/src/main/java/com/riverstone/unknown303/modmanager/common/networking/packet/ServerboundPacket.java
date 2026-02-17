package com.riverstone.unknown303.modmanager.common.networking.packet;

import com.riverstone.unknown303.modmanager.common.networking.context.base.PacketContext;
import com.riverstone.unknown303.modmanager.common.networking.context.base.ServerPacketContext;

public abstract class ServerboundPacket<SELF extends ServerboundPacket<SELF>>
        extends Packet<SELF> {

    public ServerboundPacket() {
        super(true);
    }

    @Override
    public final void runPacket(PacketContext context) {
        if (!(context instanceof ServerPacketContext serverContext))
            return;
        handle(serverContext);
    }

    public abstract void handle(ServerPacketContext context);
}
