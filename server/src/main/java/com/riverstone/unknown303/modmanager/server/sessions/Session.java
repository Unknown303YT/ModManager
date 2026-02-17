package com.riverstone.unknown303.modmanager.server.sessions;

import java.time.Instant;
import java.util.UUID;

public final class Session {
    private final UUID userId;
    private final String deviceId;
    private final String tokenHash;
    private final Instant expiresAt;

    public Session(UUID userId, String deviceId, String tokenHash, Instant expiresAt) {
        this.userId = userId;
        this.deviceId = deviceId;
        this.tokenHash = tokenHash;
        this.expiresAt = expiresAt;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getTokenHash() {
        return tokenHash;
    }

    public Instant expiresAt() {
        return expiresAt;
    }
}
