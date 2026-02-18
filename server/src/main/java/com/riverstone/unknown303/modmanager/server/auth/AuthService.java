package com.riverstone.unknown303.modmanager.server.auth;

import com.riverstone.unknown303.modmanager.common.global.CryptoUtil;
import com.riverstone.unknown303.modmanager.common.networking.packet.custom.client.ClientboundLoginSuccessPacket;
import com.riverstone.unknown303.modmanager.common.networking.packet.status.ClientboundStatusPacket;
import com.riverstone.unknown303.modmanager.common.networking.packet.status.StatusCode;
import com.riverstone.unknown303.modmanager.common.networking.packet.status.StatusType;
import com.riverstone.unknown303.modmanager.common.user.UserIdentity;
import com.riverstone.unknown303.modmanager.server.Server;
import com.riverstone.unknown303.modmanager.server.sessions.SessionManager;
import com.riverstone.unknown303.modmanager.server.user.InMemoryUserStore;
import com.riverstone.unknown303.modmanager.server.user.ServerUser;
import com.riverstone.unknown303.modmanager.server.user.UserStore;

import java.util.Map;
import java.util.UUID;

public final class AuthService {
    public static final AuthService INSTANCE = new AuthService();

    private final UserStore users = new InMemoryUserStore();
    private final SessionManager sessions = new SessionManager();

    private AuthService() {}

    public void createAccount(String clientAddress, String username, String password, String deviceId, UUID requestId) {
        if (users.getByUsername(username) != null) {
            Server.sendPacket(clientAddress, new ClientboundStatusPacket(
                    StatusType.ERROR,
                    StatusCode.ACCOUNT_CREATION_FAILED,
                    "Username already exists!",
                    requestId,
                    Map.of("field", "username",
                            "reason", "already_exists")
            ));
            return;
        }

        UUID id = UUID.randomUUID();
        String hash = CryptoUtil.hashPassword(password);

        ServerUser user = new ServerUser(id, username, hash);
        users.save(user);

        UserIdentity identity = user.toIdentity();
        String token = sessions.createSession(id, deviceId, false);

        Server.sendPacket(clientAddress, new ClientboundLoginSuccessPacket(identity, token));

        Server.sendPacket(clientAddress, new ClientboundStatusPacket(
                StatusType.SUCCESS,
                StatusCode.ACCOUNT_CREATED,
                "Account created successfully",
                requestId,
                Map.of("userId", id.toString())
        ));
    }

    public void login(String clientAddress, String username, String password, String deviceId, boolean rememberMe, UUID requestId) {
        ServerUser user = users.getByUsername(username);
        if (user == null || !CryptoUtil.verifyPassword(password, user.getPasswordHash())) {
            Server.sendPacket(clientAddress, new ClientboundStatusPacket(
                    StatusType.ERROR,
                    StatusCode.LOGIN_FAILED,
                    "Invalid username or password",
                    requestId,
                    Map.of()
            ));
            return;
        }

        String token = sessions.createSession(user.getId(), deviceId, rememberMe);
        UserIdentity identity = user.toIdentity();

        Server.sendPacket(clientAddress, new ClientboundLoginSuccessPacket(identity, token));

        Server.sendPacket(clientAddress, new ClientboundStatusPacket(
                StatusType.SUCCESS,
                StatusCode.LOGIN_SUCCESS,
                "Login successful",
                requestId,
                Map.of()
        ));
    }
}
