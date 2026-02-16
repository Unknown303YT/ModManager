package com.riverstone.unknown303.modmanager.data;

import com.google.gson.*;
import com.riverstone.unknown303.modmanager.global.Identifier;

import java.util.*;

public class Database<T> {
    public static final SaveCo


    private final List<T> data = new ArrayList<>();
    private final DataBuilder<T> builder;

    public Database(DataBuilder<T> builder) {
        this.builder = builder;
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

    public int size() {
        return data.size();
    }

    public List<T> getData() {
        return Collections.unmodifiableList(data);
    }

    public JsonElement encode() {
        FriendlyNetworker networker = FriendlyNetworker.writer();
        networker.writeInt(size());
        for (T value : data)
            networker.writeNetworker(builder.build(value));
        JsonObject json = new JsonObject();
        json.addProperty("builder_id", builder.getId().toString());
        return networker.encode(json);
    }

    @SuppressWarnings("unchecked")
    public static <T> Database<T> decode(JsonElement json) {
        JsonObject data = json.getAsJsonObject();
        FriendlyNetworker networker =
                FriendlyNetworker.reader(data);

        DataBuilder<T> builder = (DataBuilder<T>) DataBuilder.BUILDERS.get(
                Identifier.parse(data.getAsJsonPrimitive(
                        "builder_id").getAsString()));
        int size = networker.readInt();

        Database<T> database = new Database<>(builder);
        for (int i = 0; i < size; i++)
            database.add(builder.build(networker.readNetworker()));

        return database;
    }

    public DataBuilder<T> getBuilder() {
        return builder;
    }
}
