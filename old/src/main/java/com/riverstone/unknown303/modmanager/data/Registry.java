package com.riverstone.unknown303.modmanager.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.riverstone.unknown303.modmanager.global.*;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Registry<T> {
    private final Identifier id;
    private final Database<Map.Entry<Identifier, T>> entries;

    public Registry(Identifier id, DataBuilder<Map.Entry<Identifier, T>> builder) {
        this.id = id;
        entries = new Database<>(builder);
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

    public List<Map.Entry<Identifier, T>> getEntries() {
        return Collections.unmodifiableList(entries.getData());
    }

    public Database<Map.Entry<Identifier, T>> getDatabase() {
        Database<Map.Entry<Identifier, T>> database = new Database<>(
                entries.getBuilder());
        for (Map.Entry<Identifier, T> entry : entries.getData())
            database.add(Map.entry(entry.getKey(), entry.getValue()));

        return database;
    }

    public JsonElement encode() {
        return entries.encode();
    }
}
