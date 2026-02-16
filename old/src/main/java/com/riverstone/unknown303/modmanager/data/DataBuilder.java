package com.riverstone.unknown303.modmanager.data;

import com.riverstone.unknown303.modmanager.global.Identifier;
import com.riverstone.unknown303.modmanager.global.Util;
import com.riverstone.unknown303.modmanager.networking.FriendlyByteBuf;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class DataBuilder<DATA> {
    public static final Map<Identifier, DataBuilder<?>> BUILDERS =
            new HashMap<>();

    public static <DATA> DataBuilder<DATA> register(Identifier id,
                                                    DataBuilder<DATA> builder) {
        BUILDERS.put(id, builder);
        return builder;
    }

    public static <DATA> DataBuilder<DATA> builder(
            Class<? super DATA> dataType) {
        return new DataBuilder<>(dataType);
    }

    public static <DATA> DataBuilder<Map.Entry<Identifier, DATA>> toDatabaseBuilder(
            DataBuilder<DATA> valueBuilder) {
        DataBuilder<Map.Entry<Identifier, DATA>> builder =
                DataBuilder.builder(Map.Entry.class);
        builder.encoder((entry, buf) -> {
            FriendlyByteBuf valueBuf = new FriendlyByteBuf(Unpooled.buffer());
            valueBuf.writeUtf(entry.getKey().toString());
            valueBuf.writeBytes(valueBuilder.build(entry.getValue()));
            buf.writeBytes(valueBuf);
        });
        builder.decoder(buf -> {
            FriendlyByteBuf valueBuf = new FriendlyByteBuf(buf.readBytes(Unpooled.buffer()));
            Identifier id = Identifier.parse(valueBuf.readUtf());
            FriendlyByteBuf valueData = new FriendlyByteBuf(valueBuf.readBytes(Unpooled.buffer()));
            return Map.entry(id, valueBuilder.build(valueData));
        });
        return builder;
    }

    private BiConsumer<DATA, FriendlyByteBuf> encoder;
    private Function<FriendlyByteBuf, DATA> decoder;

    public DataBuilder(Class<? super DATA> packetType) {}

    public DataBuilder<DATA> encoder(BiConsumer<DATA, FriendlyByteBuf> encoder) {
        this.encoder = encoder;
        return this;
    }

    public DataBuilder<DATA> decoder(Function<FriendlyByteBuf, DATA> decoder) {
        this.decoder = decoder;
        return this;
    }

    public FriendlyByteBuf build(DATA data) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        encoder.accept(data, buf);
        return buf;
    }

    public DATA build(FriendlyByteBuf buf) {
        return decoder.apply(buf);
    }

    public Identifier getId() {
        return Util.invertMap(BUILDERS).get(this);
    }
}
