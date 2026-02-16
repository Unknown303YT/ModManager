package com.riverstone.unknown303.modmanager.common.networking.packet;

public abstract class ClientboundPacket<SELF extends ClientboundPacket<SELF>>
        extends Packet<SELF> {
    public ClientboundPacket() {
        super(false);
    }
}
