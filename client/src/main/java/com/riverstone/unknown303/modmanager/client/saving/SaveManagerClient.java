package com.riverstone.unknown303.modmanager.client.saving;

import com.riverstone.unknown303.modmanager.common.data.saving.SaveManagerCommon;
import com.riverstone.unknown303.modmanager.common.global.Logger;

import java.io.IOException;
import java.util.Map;

public class SaveManagerClient {
    private final SaveManagerCommon common;

    public SaveManagerClient() {
        common = new SaveManagerCommon();
    }

    public void writeDeviceId(String deviceId) {
        try {
            common.writeJson(common.resolve("device.json"), Map.of("deviceId", deviceId));
        } catch (IOException e) {
            Logger.getLogger().error("Exception encountered while writing device ID", e);
        }
    }

    public String readDeviceId() {
        try {
            Map<?, ?> json = common.readJson(common.resolve("device.json"), Map.class);
            return (String) json.get("deviceId");
        } catch (IOException e) {
            Logger.getLogger().error("Exception encountered while reading device ID", e);
            return null;
        }
    }

    public void writeToken(String token) {
        try {
            common.writeJson(common.resolve("token.json"), Map.of("token", token));
        } catch (IOException e) {
            Logger.getLogger().error("Exception encountered while writing session token", e);
        }
    }

    public String readToken() {
        try {
            Map<?, ?> json = common.readJson(common.resolve("token.json"), Map.class);
            return (String) json.get("token");
        } catch (IOException e) {
            Logger.getLogger().error("Exception encountered while reading session token", e);
            return null;
        }
    }
}
