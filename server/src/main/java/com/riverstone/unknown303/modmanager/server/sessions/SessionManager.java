package com.riverstone.unknown303.modmanager.server.sessions;

import com.riverstone.unknown303.modmanager.common.global.CryptoUtil;
import com.riverstone.unknown303.modmanager.common.global.Logger;
import com.riverstone.unknown303.modmanager.common.user.Users;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SessionManager {
    private final Map<String, Session> sessions = new HashMap<>();
    private static final Duration DEFAULT_TIMEOUT = Duration.ofHours(8);
    private static final Duration REMEMBER_ME_TIMEOUT = Duration.ofDays(14);

    public String createSession(UUID userId, String deviceId, boolean rememberMe) {
        if (userId.equals(Users.SYSTEM.getId())) {
            Logger.getLogger().error("SYSTEM cannot be used externally!", IllegalArgumentException::new);
            return null;
        }

        String token = CryptoUtil.generateToken();
        String tokenHash = CryptoUtil.hashToken(token);

        Instant expiresAt = Instant.now().plus(rememberMe ? REMEMBER_ME_TIMEOUT : DEFAULT_TIMEOUT);
        Session session = new Session(userId, tokenHash, deviceId, expiresAt);
        sessions.put(tokenHash, session);

        return token;
    }

    public UUID authenticate(String token, String deviceId) {
        if (token == null) {
            Logger.getLogger().error("Token cannot be null!", NullPointerException::new);
            return null;
        }

        String tokenHash = CryptoUtil.hashToken(token);
        Session session = sessions.get(tokenHash);
        if (session == null)
            return null;
        if (!session.getDeviceId().equals(deviceId))
            return null;

        if (session.expiresAt().isBefore(Instant.now())) {
            sessions.remove(tokenHash);
            return null;
        }

        return session.getUserId();
    }

    public void invalidate(String token) {
        if (token == null) {
            Logger.getLogger().error("Token cannot be null!", NullPointerException::new);
            return;
        }

        String tokenHash = CryptoUtil.hashToken(token);
        sessions.remove(tokenHash);
    }
}
