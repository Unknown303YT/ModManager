package com.riverstone.unknown303.modmanager.common.data;

import com.google.gson.*;
import com.riverstone.unknown303.modmanager.common.global.Identifier;
import com.riverstone.unknown303.modmanager.common.global.Util;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class SaveCodec<DATA> {
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create();

    public static final Map<Identifier, SaveCodec<?>> BUILDERS =
            new HashMap<>();

    public static <DATA> SaveCodec<DATA> register(Identifier id,
                                                  SaveCodec<DATA> builder) {
        BUILDERS.put(id, builder);
        return builder;
    }

    public static <DATA> SaveCodec<DATA> builder(
            Class<? super DATA> dataType) {
        return new SaveCodec<>(dataType);
    }

    public static <DATA> SaveCodec<Map.Entry<Identifier, DATA>> toDatabaseBuilder(
            SaveCodec<DATA> valueBuilder) {
        SaveCodec<Map.Entry<Identifier, DATA>> builder =
                SaveCodec.builder(Map.Entry.class);
        builder.encoder((entry, json) -> {
            json.addProperty("id", entry.getKey().toString());
            json.add("value", valueBuilder.build(entry.getValue()));
        });
        builder.decoder(json -> {
            Identifier id = Identifier.parse(json.getAsJsonPrimitive("id").getAsString());
            DATA value = valueBuilder.build(json.getAsJsonObject("value"));
            return Map.entry(id, value);
        });
        return builder;
    }

    private BiConsumer<DATA, JsonObject> encoder;
    private Function<JsonObject, DATA> decoder;

    private SaveCodec(Class<? super DATA> packetType) {}

    public SaveCodec<DATA> encoder(BiConsumer<DATA, JsonObject> encoder) {
        this.encoder = encoder;
        return this;
    }

    public SaveCodec<DATA> decoder(Function<JsonObject, DATA> decoder) {
        this.decoder = decoder;
        return this;
    }

    public JsonObject build(DATA data) {
        JsonObject json = new JsonObject();
        encoder.accept(data, json);
        return json;
    }

    public DATA build(JsonObject json) {
        return decoder.apply(json);
    }

    public void write(Path file, DATA data) throws IOException {
        JsonObject json = build(data);
        try (Writer writer = Files.newBufferedWriter(file)) {
            GSON.toJson(json, writer);
        }
    }

    public DATA read(Path file) throws IOException {
        try (Reader reader = Files.newBufferedReader(file)) {
            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
            return build(json);
        }
    }

    public Identifier getId() {
        return Util.invertMap(BUILDERS).get(this);
    }
}
