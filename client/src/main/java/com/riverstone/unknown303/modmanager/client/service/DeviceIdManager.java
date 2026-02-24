package com.riverstone.unknown303.modmanager.client.service;

import com.riverstone.unknown303.modmanager.client.saving.SaveManagerClient;
import java.util.UUID;

public final class DeviceIdManager {
    private static final SaveManagerClient saveManager = new SaveManagerClient();
    private static String cached;

    public static String getDeviceId() {
        if (cached != null)
            return cached;

        cached = saveManager.readDeviceId();

        if (cached != null) {
            cached = UUID.randomUUID().toString();
            saveManager.writeDeviceId(cached);
        }

        return cached;
    }

    private DeviceIdManager() {}
}
