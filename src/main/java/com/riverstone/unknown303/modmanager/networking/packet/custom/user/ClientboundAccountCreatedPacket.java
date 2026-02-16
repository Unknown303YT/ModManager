package com.riverstone.unknown303.modmanager.networking.packet.custom.user;

import com.riverstone.unknown303.modmanager.data.NetworkCodec;
import com.riverstone.unknown303.modmanager.global.Identifier;
import com.riverstone.unknown303.modmanager.user.User;
import com.riverstone.unknown303.modmanager.user.Users;
import com.riverstone.unknown303.modmanager.networking.FriendlyByteBuf;
import com.riverstone.unknown303.modmanager.networking.packet.ClientboundPacket;
import com.riverstone.unknown303.modmanager.networking.packet.Packets;

import java.util.Map;

public class ClientboundAccountCreatedPacket extends ClientboundPacket<ClientboundAccountCreatedPacket> {
    private final User user;

    public ClientboundAccountCreatedPacket(User user) {
        this.user = user;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBytes(Users.NETWORK_CODEC.build(user));
    }

    public static ClientboundAccountCreatedPacket decode(FriendlyByteBuf buf) {
        return new ClientboundAccountCreatedPacket(Users.NETWORK_CODEC.build(buf));
    }

    @Override
    public NetworkCodec<ClientboundAccountCreatedPacket> getCodec() {
        return Packets.CLIENTBOUND_ACCOUNT_CREATED;
    }

    @Override
    public String handle(String clientAddress) {
        Users.USERS.add(Map.entry(new Identifier(Users.SYSTEM, user.getUsername()), user));

        return "SUCCESS";
    }
}
