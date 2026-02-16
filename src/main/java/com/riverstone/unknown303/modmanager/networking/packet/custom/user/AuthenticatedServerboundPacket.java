package com.riverstone.unknown303.modmanager.networking.packet.custom.user;

import com.riverstone.unknown303.modmanager.networking.packet.ServerboundPacket;
import com.riverstone.unknown303.modmanager.user.ServerUser;

public abstract class AuthenticatedServerboundPacket<SELF extends AuthenticatedServerboundPacket<SELF>> extends ServerboundPacket<SELF> {
    protected final String token;

    protected AuthenticatedServerboundPacket(String token) {
        this.token = token;
    }

    protected ServerUser requireUser
}
