package com.riverstone.unknown303.modmanager.networking.packet;

import com.riverstone.unknown303.modmanager.data.DataBuilder;
import com.riverstone.unknown303.modmanager.global.Identifier;
import com.riverstone.unknown303.modmanager.global.User;
import com.riverstone.unknown303.modmanager.global.Users;
import com.riverstone.unknown303.modmanager.networking.packet.custom.ClientboundCreateAccountPacket;
import com.riverstone.unknown303.modmanager.networking.packet.custom.ServerboundCreateAccountPacket;
import com.riverstone.unknown303.modmanager.data.Registries;

import java.util.List;

public class Packets {
    public static final DataBuilder<ServerboundCreateAccountPacket>
            SERVERBOUND_CREATE_ACCOUNT =
            DataBuilder.builder(ServerboundCreateAccountPacket.class)
                    .encoder(ServerboundCreateAccountPacket::encode)
                    .decoder(ServerboundCreateAccountPacket::decode);
    public static final DataBuilder<ClientboundCreateAccountPacket>
            CLIENTBOUND_CREATE_ACCOUNT =
            DataBuilder.builder(ClientboundCreateAccountPacket.class)
                    .encoder(ClientboundCreateAccountPacket::encode)
                    .decoder(ClientboundCreateAccountPacket::decode);

    public static void register() {
        DataBuilder.BUILDERS.put(
                new Identifier(Users.SYSTEM, "serverbound_create_account"),
                SERVERBOUND_CREATE_ACCOUNT);
        DataBuilder.BUILDERS.put(
                new Identifier(Users.SYSTEM, "clientbound_create_account"),
                CLIENTBOUND_CREATE_ACCOUNT);
    }
}
