package com.riverstone.unknown303.modmanager.common.registry;

import com.google.gson.JsonElement;
import com.riverstone.unknown303.modmanager.common.data.Database;
import com.riverstone.unknown303.modmanager.common.data.NetworkCodec;
import com.riverstone.unknown303.modmanager.common.data.SaveCodec;
import com.riverstone.unknown303.modmanager.common.global.Identifier;
import com.riverstone.unknown303.modmanager.common.global.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Registry<T> {
    private final Identifier id;

    private final SaveCodec<Map.Entry<Identifier, T>> saveCodec;
    private final NetworkCodec<Map.Entry<Identifier, T>> networkCodec;

    private final Database<Map.Entry<Identifier, T>> entries;

    public Registry(Identifier id, SaveCodec<T> saveCodec, NetworkCodec<T> networkCodec) {
        this.id = id;
        this.saveCodec = SaveCodec.toDatabaseBuilder(saveCodec);
        this.networkCodec = NetworkCodec.toDatabaseBuilder(networkCodec);
        this.entries = new Database<>(this.saveCodec);
    }

    public Identifier getId() {
        return id;
    }

    public boolean containsKey(Identifier key) {
        for (Map.Entry<Identifier, T> entry : entries.getData()) {
            if (entry.getKey().equals(key))
                return true;
        }
        return false;
    }

    public boolean containsValue(T value) {
        for (Map.Entry<Identifier, T> entry : entries.getData()) {
            if (entry.getValue().equals(value))
                return true;
        }
        return false;
    }

    public Identifier getKey(T value) {
        for (Map.Entry<Identifier, T> entry : entries.getData()) {
            if (entry.getValue().equals(value))
                return entry.getKey();
        }
        return null;
    }

    public T getValue(Identifier key) {
        for (Map.Entry<Identifier, T> entry : entries.getData()) {
            if (entry.getKey().equals(key))
                return entry.getValue();
        }
        return null;
    }

    public <E extends T> E register(Identifier key, E value) {
        if (key == null)
            Logger.getLogger().error("Can't use a null key for the registry!");
        if (value == null)
            Logger.getLogger().error("Can't add a null object to the registry!");
        if (containsKey(key) && getValue(key).equals(value))
            Logger.getLogger().warn(MessageFormat.format("Registry {0}: " +
                            "The object {1} has previously been registered for the " +
                            "same name {2}.", getId(), value, key));
        entries.add(Map.entry(key, value));
        return value;
    }

    public void clear() {
        entries.clear();
    }

    public List<Map.Entry<Identifier, T>> getEntries() {
        return Collections.unmodifiableList(entries.getData());
    }

    public Database<Map.Entry<Identifier, T>> copyDatabase() {
        Database<Map.Entry<Identifier, T>> copy = new Database<>(
                entries.getSaveCodec());
        for (Map.Entry<Identifier, T> entry : entries.getData())
            copy.add(Map.entry(entry.getKey(), entry.getValue()));

        return copy;
    }

    public JsonElement encode() {
        return entries.encode();
    }

    public void save(Path folder) throws IOException {
        entries.saveAllToFolder(folder, entry -> entry.getKey().toFileSafeString());
    }

    public void load(Path folder) throws IOException {
        entries.loadAllFromFolder(folder);
    }
}
