package com.riverstone.unknown303.modmanager.networking.packet.custom;

import com.riverstone.unknown303.modmanager.global.Identifier;
import com.riverstone.unknown303.modmanager.global.Logger;
import com.riverstone.unknown303.modmanager.global.User;
import com.riverstone.unknown303.modmanager.global.Users;
import com.riverstone.unknown303.modmanager.networking.FriendlyByteBuf;
import com.riverstone.unknown303.modmanager.networking.packet.ClientboundPacket;
import com.riverstone.unknown303.modmanager.networking.packet.Packets;
import io.netty.buffer.ByteBuf;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ClientboundCreateAccountPacket extends
        ClientboundPacket<ClientboundCreateAccountPacket> {
    private final String username;
    private final String token;

    public ClientboundCreateAccountPacket(String username, String token) {
        this.username = username;
        this.token = token;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(username);
        buf.writeUtf(token);
    }

    public static ClientboundCreateAccountPacket decode(FriendlyByteBuf buf) {
        String username = buf.readUtf();
        String token = buf.readUtf();
        return new ClientboundCreateAccountPacket(username, token);
    }

    @Override
    public DataBuilder<ClientboundCreateAccountPacket> getPacketBuilder() {
        return Packets.CLIENTBOUND_CREATE_ACCOUNT;
    }

    @Override
    public String handle(String clientAddress) {
        for (Map.Entry<Identifier, User> entry : Users.USERS.getData()) {
            User user = entry.getValue();
            if (Objects.equals(user.getUsername(), username)) {
                Logger.getLogger("Client Handler Thread")
                        .error("User with username " + username + " already exists!");
                return "FAIL USER_EXISTS";
            }
        }

        User registered = new User(username, "", "", token, false);
        Users.USERS.add(Map.entry(new Identifier(Users.SYSTEM, username), registered));
        return "SUCCESS " + Users.USER_BUILDER.build(registered);
    }

    @Override
    public List<String> validResponses() {
        return List.of("SUCCESS", "FAIL");
    }
}
