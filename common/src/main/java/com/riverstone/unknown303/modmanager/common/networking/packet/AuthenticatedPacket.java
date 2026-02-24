package com.riverstone.unknown303.modmanager.common.networking.packet;

public abstract class AuthenticatedPacket<SELF extends AuthenticatedPacket<SELF>> extends ServerboundPacket<SELF> {
    protected String token;
    protected String deviceId;

    public String getToken() {
        return token;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
