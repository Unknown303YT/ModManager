package com.riverstone.unknown303.modmanager.common.data.saving;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

public class SaveManagerCommon {
    private final Path root;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public SaveManagerCommon() {
        this(ModManagerPaths.getRootFolder());
    }

    public SaveManagerCommon(Path root) {
        this.root = root;
    }

    public Path getRoot() {
        return root;
    }

    public Path resolve(String... parts) {
        Path p = root;
        for (String s : parts)
            p = p.resolve(s);
        return p;
    }

    public static void ensureFolder(Path path) throws IOException {
        if (!Files.exists(path))
            Files.createDirectories(path);
    }

    public boolean exists(Path file) {
        return Files.exists(file);
    }

    public void deleteFolder(Path folder) throws IOException {
        if (!exists(folder))
            return;
        Files.walk(folder)
                .sorted(Comparator.reverseOrder())
                .forEach(path -> path.toFile().delete());
    }

    public void writeJson(Path file, Object data) throws IOException {
        ensureFolder(file.getParent());
        Files.writeString(file, gson.toJson(data));
    }

    public <T> T readJson(Path file, Class<T> type) throws IOException {
        return gson.fromJson(Files.readString(file), type);
    }
}
