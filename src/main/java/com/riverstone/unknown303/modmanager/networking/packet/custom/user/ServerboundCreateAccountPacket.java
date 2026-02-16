package com.riverstone.unknown303.modmanager.networking.packet.custom.user;

import com.riverstone.unknown303.modmanager.data.NetworkCodec;
import com.riverstone.unknown303.modmanager.global.*;
import com.riverstone.unknown303.modmanager.networking.FriendlyByteBuf;
import com.riverstone.unknown303.modmanager.networking.packet.Packets;
import com.riverstone.unknown303.modmanager.networking.server.Server;
import com.riverstone.unknown303.modmanager.networking.packet.ServerboundPacket;
import com.riverstone.unknown303.modmanager.user.User;
import com.riverstone.unknown303.modmanager.user.Users;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;

public class ServerboundCreateAccountPacket extends ServerboundPacket<ServerboundCreateAccountPacket> {
    private final String username;
    private final String password;

    public ServerboundCreateAccountPacket(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(username);
        buf.writeUtf(password);
    }

    public static ServerboundCreateAccountPacket decode(FriendlyByteBuf buf) {
        return new ServerboundCreateAccountPacket(buf.readUtf(), buf.readUtf());
    }

    @Override
    public NetworkCodec<ServerboundCreateAccountPacket> getCodec() {
        return Packets.SERVERBOUND_CREATE_ACCOUNT;
    }

    @Override
    public String handle(String clientAddress) {
        for (Map.Entry<Identifier, User> entry : Users.USERS.getData())
            if (entry.getValue().getUsername().equals(username))
                return "FAIL USER_EXISTS";

        try {
            String hash = CryptoUtil.hashPassword(password);
            String token = CryptoUtil.generateToken();

            User user = User.onServer(username, salt, hash, CryptoUtil.hashToken(token));

            Users.USERS.add(Map.entry(new Identifier(Users.SYSTEM, username), user));
            Users.save();

            Server.sendPacket(clientAddress,
                    new ClientboundAccountCreatedPacket(user));

            return "SUCCESS";
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            Logger.getLogger().fatal(e);
            return "FAIL EXCEPTION";
        }
    }
}
