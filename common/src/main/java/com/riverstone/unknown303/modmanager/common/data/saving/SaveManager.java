package com.riverstone.unknown303.modmanager.common.data.saving;

import com.riverstone.unknown303.modmanager.common.global.Identifier;
import com.riverstone.unknown303.modmanager.common.global.Logger;
import com.riverstone.unknown303.modmanager.common.registry.Registry;
import com.riverstone.unknown303.modmanager.common.registry.RegistryManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SaveManager {
    private final Path root;

    public SaveManager() {
        this(ModManagerPaths.getRootFolder());
    }

    public SaveManager(Path root) {
        this.root = root;
    }

    public Path getRoot() {
        return root;
    }

    public Path getProjectsFolder() {
        return root.resolve("projects");
    }

    public Path getProjectFolder(Identifier projectId) {
        return getProjectsFolder().resolve(projectId.toFileSafeString());
    }

    public Path getRegistryFolder(Identifier projectId, Identifier registryId) {
        return getProjectFolder(projectId).resolve(registryId.toFileSafeString());
    }

    public static void ensureFolder(Path path) throws IOException {
        if (!Files.exists(path))
            Files.createDirectories(path);
    }

    public void saveProject(Identifier projectId) {
        try {
            for (Registry<?> registry : RegistryManager.getAll()) {
                Path folder = getRegistryFolder(projectId, registry.getId());
                ensureFolder(folder);
                registry.save(folder);
            }
        } catch (IOException e) {
            Logger.getLogger().error("Failed to save project " + projectId, e);
        }
    }

    public void loadProject(Identifier projectId) {
        Path projectFolder = getProjectFolder(projectId);

        if (!Files.exists(projectFolder)) {
            Logger.getLogger().warn("Project folder does not exist: " + projectFolder);
            return;
        }

        for (Registry<?> registry : RegistryManager.getAll()) {
            Path folder = getRegistryFolder(projectId, registry.getId());
            try {
                registry.load(folder);
            } catch (IOException e) {
                Logger.getLogger().error("Failed to load registry " + registry.getId(), e);
            }
        }
    }
}
