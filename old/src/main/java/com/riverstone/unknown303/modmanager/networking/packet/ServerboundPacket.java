package com.riverstone.unknown303.modmanager.networking.packet;

public abstract class ServerboundPacket<SELF extends ServerboundPacket<SELF>>
        extends Packet<SELF> {
    public ServerboundPacket() {
        super(true);
    }
}
