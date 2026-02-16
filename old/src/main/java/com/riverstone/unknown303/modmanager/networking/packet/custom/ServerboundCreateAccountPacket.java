package com.riverstone.unknown303.modmanager.networking.packet.custom;

import com.riverstone.unknown303.modmanager.global.*;
import com.riverstone.unknown303.modmanager.networking.FriendlyByteBuf;
import com.riverstone.unknown303.modmanager.networking.Server;
import com.riverstone.unknown303.modmanager.networking.packet.Packets;
import com.riverstone.unknown303.modmanager.networking.packet.ServerboundPacket;
import io.netty.buffer.ByteBuf;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ServerboundCreateAccountPacket extends
        ServerboundPacket<ServerboundCreateAccountPacket> {
    private final String username;
    private final String password;
    private User result = null;

    public ServerboundCreateAccountPacket(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(username);
        buf.writeUtf(password);
    }

    public static ServerboundCreateAccountPacket decode(FriendlyByteBuf buf) {
        String username = buf.readUtf();
        String password = buf.readUtf();
        return new ServerboundCreateAccountPacket(username, password);
    }

    @Override
    public DataBuilder<ServerboundCreateAccountPacket> getPacketBuilder() {
        return Packets.SERVERBOUND_CREATE_ACCOUNT;
    }

    @Override
    public String handle(String clientAddress) {
        for (User user : Users.USERS.getData().stream().map(
                Map.Entry::getValue).toList()) {
            if (Objects.equals(user.getUsername(), username)) {
                Logger.getLogger("Client Handler Thread")
                        .error("User with username " + username + " already exists!");
                return "FAIL USER_EXISTS";
            }
        }

        try {
            String salt = CryptoUtil.generateSalt();
            String hash = CryptoUtil.hashPassword(password, salt);
            String token = CryptoUtil.generateToken();
            User user = User.onServer(username, salt, hash,
                    CryptoUtil.hashToken(token));
            Users.USERS.add(Map.entry(new Identifier(Users.SYSTEM, username),
                    user));
            String message = Server.sendPacket(clientAddress,
                    new ClientboundCreateAccountPacket(username, token));
            if (message.startsWith("FAIL"))
                return message;

            result = Users.USER_BUILDER.build(FriendlyByteBuf.reader(
                    GsonHelper.fromJsonString(message.replaceFirst("SUCCESS ",
                            "")).getAsJsonObject()));

            Logger.getLogger().info(MessageFormat.format("USER CREATED! Username = " +
                            "{0}, Hash = {1}, Salt = {2}, Token = {3}",
                    user.getUsername(), user.getHash(), user.getSalt(), user.getToken()));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            Logger.getLogger().fatal(e);
        }

        return "SUCCESS";
    }

    @Override
    public List<String> validResponses() {
        return List.of("SUCCESS", "FAIL");
    }

    public boolean isComplete() {
        return result != null;
    }

    public User getResult() {
        return result;
    }
}
