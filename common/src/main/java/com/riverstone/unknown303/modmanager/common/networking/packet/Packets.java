package com.riverstone.unknown303.modmanager.common.networking.packet;

import com.riverstone.unknown303.modmanager.common.data.NetworkCodec;
import com.riverstone.unknown303.modmanager.common.global.Identifier;
import com.riverstone.unknown303.modmanager.common.networking.packet.custom.server.ServerboundLoginPacket;
import com.riverstone.unknown303.modmanager.common.networking.packet.status.ClientboundStatusPacket;
import com.riverstone.unknown303.modmanager.common.user.Users;

public class Packets {
    public static final NetworkCodec<ClientboundStatusPacket> STATUS_PACKET =
            NetworkCodec.builder(ClientboundStatusPacket.class)
                    .encoder(ClientboundStatusPacket::encode)
                    .decoder(ClientboundStatusPacket::decode);
    public static final NetworkCodec<ServerboundLoginPacket> SERVERBOUND_LOGIN_PACKET =
            NetworkCodec.builder(ServerboundLoginPacket.class)
                    .encoder(ServerboundLoginPacket::encode)
                    .decoder(ServerboundLoginPacket::decode);

    public static void register() {
        NetworkCodec.register(new Identifier(Users.SYSTEM, "status_packet"),
                STATUS_PACKET);
        NetworkCodec.register(new Identifier(Users.SYSTEM, "serverbound_login_packet"),
                SERVERBOUND_LOGIN_PACKET);
    }
}
