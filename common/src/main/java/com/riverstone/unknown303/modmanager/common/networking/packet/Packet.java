package com.riverstone.unknown303.modmanager.common.networking.packet;

import com.riverstone.unknown303.modmanager.common.data.NetworkCodec;
import com.riverstone.unknown303.modmanager.common.global.Logger;
import com.riverstone.unknown303.modmanager.common.networking.context.base.PacketContext;

import java.util.UUID;

public abstract class Packet<SELF extends Packet<SELF>> {
    private final boolean isFromClient;
    private UUID requestId;

    public Packet(boolean isFromClient) {
        this.isFromClient = isFromClient;
        this.requestId = UUID.randomUUID();
        Logger.getLogger().info("Created Packet " + this.getClass().getSimpleName() + " with ID " + requestId);
    }

    public final UUID getRequestId() {
        return requestId;
    }

    public final void setRequestId(UUID requestId) {
        this.requestId = requestId;
    }

    public abstract NetworkCodec<SELF> getCodec();

    public abstract void runPacket(PacketContext context);

    public final boolean isFromClient() {
        return isFromClient;
    }
}
