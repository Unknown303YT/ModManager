package com.riverstone.unknown303.modmanager.common.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.riverstone.unknown303.modmanager.common.data.saving.SaveManager;
import com.riverstone.unknown303.modmanager.common.global.Identifier;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class Database<T> {
    private final List<T> data = new ArrayList<>();
    private final SaveCodec<T> saveCodec;

    public Database(SaveCodec<T> saveCodec) {
        this.saveCodec = saveCodec;
    }

    public boolean contains(T value) {
        return data.contains(value);
    }

    public T get(int index) {
        return data.get(index);
    }

    public void add(T value) {
        data.add(value);
    }

    public void remove(int index) {
        data.remove(index);
    }

    public void remove(T value) {
        data.remove(value);
    }

    public void clear() {
        data.clear();
    }

    public int size() {
        return data.size();
    }

    public List<T> getData() {
        return Collections.unmodifiableList(data);
    }

    public JsonElement encode() {
        JsonObject root = new JsonObject();
        root.addProperty("id", saveCodec.getId().toString());

        JsonArray array = new JsonArray();
        for (T value : data)
            array.add(saveCodec.build(value));

        root.add("values", array);
        return root;
    }

    @SuppressWarnings("unchecked")
    public static <T> Database<T> decode(JsonElement json) {
        JsonObject data = json.getAsJsonObject();

        Identifier id = Identifier.parse(data.get("id").getAsString());
        SaveCodec<T> builder =
                (SaveCodec<T>) SaveCodec.BUILDERS.get(id);

        Database<T> database = new Database<>(builder);

        JsonArray array = data.getAsJsonArray("values");
        for (JsonElement element : array)
            database.add(builder.build(element.getAsJsonObject()));

        return database;
    }

    public SaveCodec<T> getSaveCodec() {
        return saveCodec;
    }

    public void saveAllToFolder(Path folder, Function<T, String> fileNamer) throws IOException {
        SaveManager.ensureFolder(folder);

        for (T value : data) {
            Path file = folder.resolve(fileNamer.apply(value) + ".json");
            saveCodec.write(file, value);
        }
    }

    public void loadAllFromFolder(Path folder) throws IOException {
        if (!Files.exists(folder))
            return;

        clear();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(folder, "*.json")) {
            for (Path file : stream) {
                T value = saveCodec.read(file);
                add(value);
            }
        }
    }
}
