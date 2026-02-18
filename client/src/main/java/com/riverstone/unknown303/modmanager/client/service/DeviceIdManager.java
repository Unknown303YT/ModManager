package com.riverstone.unknown303.modmanager.client.service;

import com.riverstone.unknown303.modmanager.common.data.saving.ModManagerPaths;
import com.riverstone.unknown303.modmanager.common.data.saving.SaveManager;
import com.riverstone.unknown303.modmanager.common.global.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.UUID;

public final class DeviceIdManager {
    private static final String FILE_NAME = "device.id";
    private static String cached;

    public static String getDeviceId() {
        if (cached != null)
            return cached;

        Path root = ModManagerPaths.getRootFolder();
        Path file = root.resolve(FILE_NAME);

        try {
            SaveManager.ensureFolder(root);

            if (Files.exists(file)) {
                cached = Files.readString(file).trim();
                if (!cached.isEmpty())
                    return cached;
            }

            cached = UUID.randomUUID().toString();
            Files.writeString(file, cached, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

            return cached;
        } catch (IOException e) {
            Logger.getLogger().error("Failed to load or create device ID", e);

            cached = UUID.randomUUID().toString();
            return cached;
        }
    }

    private DeviceIdManager() {}
}
