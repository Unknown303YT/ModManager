package com.riverstone.unknown303.modmanager.common.data.saving;

import java.nio.file.Path;

public final class ModManagerPaths {
    private ModManagerPaths() {}

    public static Path getRootFolder() {
        String os = System.getProperty("os.name").toLowerCase();
        String home = System.getProperty("user.home").toLowerCase();

        if (os.contains("win")) {
            String appData = System.getProperty("APPDATA");
            if (appData != null)
                return Path.of(appData, "ModManager");
            return Path.of(home, "AppData", "Roaming", "ModManager");
        }

        if (os.contains("mac"))
            return Path.of(home, "Library", "Application Support", "ModManager");

        return Path.of(home, ".config", "ModManager");
    }
}
