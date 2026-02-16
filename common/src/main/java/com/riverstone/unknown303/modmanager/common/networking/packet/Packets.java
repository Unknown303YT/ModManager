package com.riverstone.unknown303.modmanager.common.networking.packet;

import com.riverstone.unknown303.modmanager.common.data.NetworkCodec;
import com.riverstone.unknown303.modmanager.common.global.Identifier;
import com.riverstone.unknown303.modmanager.common.user.Users;
import com.riverstone.unknown303.modmanager.common.networking.packet.custom.user.ClientboundAccountCreatedPacket;
import com.riverstone.unknown303.modmanager.common.networking.packet.custom.user.ServerboundCreateAccountPacket;
import com.riverstone.unknown303.modmanager.common.networking.netty.StatusPacket;

public class Packets {
    public static final NetworkCodec<StatusPacket> STATUS_PACKET =
        NetworkCodec.builder(StatusPacket.class)
                .encoder(StatusPacket::encode)
                .decoder(StatusPacket::decode);

    public static final NetworkCodec<ServerboundCreateAccountPacket> SERVERBOUND_CREATE_ACCOUNT =
            NetworkCodec.builder(ServerboundCreateAccountPacket.class)
                    .encoder(ServerboundCreateAccountPacket::encode)
                    .decoder(ServerboundCreateAccountPacket::decode);
    public static final NetworkCodec<ClientboundAccountCreatedPacket> CLIENTBOUND_ACCOUNT_CREATED =
            NetworkCodec.builder(ClientboundAccountCreatedPacket.class)
                    .encoder(ClientboundAccountCreatedPacket::encode)
                    .decoder(ClientboundAccountCreatedPacket::decode);

    public static void register() {
//        DataBuilder.BUILDERS.put(
//                new Identifier(Users.SYSTEM, "serverbound_create_account"),
//                SERVERBOUND_CREATE_ACCOUNT);
//        DataBuilder.BUILDERS.put(
//                new Identifier(Users.SYSTEM, "clientbound_create_account"),
//                CLIENTBOUND_CREATE_ACCOUNT);
        NetworkCodec.register(new Identifier(Users.SYSTEM, "status_packet"),
                STATUS_PACKET);
        NetworkCodec.register(new Identifier(Users.SYSTEM, "serverbound_create_account_packet"),
                SERVERBOUND_CREATE_ACCOUNT);
        NetworkCodec.register(new Identifier(Users.SYSTEM, "clientbound_account_created_packet"),
                CLIENTBOUND_ACCOUNT_CREATED);
    }
}
