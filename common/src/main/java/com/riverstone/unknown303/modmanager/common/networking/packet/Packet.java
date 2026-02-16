package com.riverstone.unknown303.modmanager.common.networking.packet;

import com.riverstone.unknown303.modmanager.common.data.NetworkCodec;
import com.riverstone.unknown303.modmanager.common.global.Logger;

public abstract class Packet<SELF extends Packet<SELF>> {
    private final boolean isFromClient;

    public Packet(boolean isFromClient) {
        this.isFromClient = isFromClient;
        Logger.getLogger().info("Created Packet " + this.getClass().getSimpleName());
    }

    public abstract NetworkCodec<SELF> getCodec();

    public abstract String handle(String clientAddress);

    public boolean isFromClient() {
        return isFromClient;
    }
}
