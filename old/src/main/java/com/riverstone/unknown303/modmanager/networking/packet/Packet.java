package com.riverstone.unknown303.modmanager.networking.packet;

import com.riverstone.unknown303.modmanager.data.DataBuilder;
import com.riverstone.unknown303.modmanager.global.Logger;

import java.util.List;

public abstract class Packet<SELF extends Packet<SELF>> {
    private final boolean isFromClient;

    public Packet(boolean isFromClient) {
        this.isFromClient = isFromClient;
        Logger.getLogger().info("Created Packet " + this.getClass().getSimpleName());
    }

    public abstract DataBuilder<SELF> getPacketBuilder();

    public abstract String handle(String clientAddress);

    public abstract List<String> validResponses();

    public boolean isFromClient() {
        return isFromClient;
    }
}
