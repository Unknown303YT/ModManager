package com.riverstone.unknown303.modmanager.common.data;

import com.riverstone.unknown303.modmanager.common.global.Identifier;
import com.riverstone.unknown303.modmanager.common.global.Util;
import com.riverstone.unknown303.modmanager.common.networking.FriendlyByteBuf;
import io.netty.buffer.Unpooled;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class NetworkCodec<DATA> {
    public static final Map<Identifier, NetworkCodec<?>> BUILDERS =
            new HashMap<>();

    public static <DATA> NetworkCodec<DATA> register(Identifier id,
                                                     NetworkCodec<DATA> builder) {
        BUILDERS.put(id, builder);
        return builder;
    }

    public static <DATA> NetworkCodec<DATA> builder(
            Class<? super DATA> dataType) {
        return new NetworkCodec<>(dataType);
    }

    public static <DATA> NetworkCodec<Map.Entry<Identifier, DATA>> toDatabaseBuilder(
            NetworkCodec<DATA> valueBuilder) {
        NetworkCodec<Map.Entry<Identifier, DATA>> builder1 =
                NetworkCodec.builder(Map.Entry.class);
        builder1.encoder((entry, buf) -> {
            FriendlyByteBuf valueBuf = new FriendlyByteBuf(Unpooled.buffer());
            valueBuf.writeUtf(entry.getKey().toString());
            valueBuf.writeBytes(valueBuilder.build(entry.getValue()));
            buf.writeBytes(valueBuf);
        });
        builder1.decoder(buf -> {
            FriendlyByteBuf valueBuf = new FriendlyByteBuf(buf.readBytes(Unpooled.buffer()));
            Identifier id = Identifier.parse(valueBuf.readUtf());
            FriendlyByteBuf valueData = new FriendlyByteBuf(valueBuf.readBytes(Unpooled.buffer()));
            return Map.entry(id, valueBuilder.build(valueData));
        });
        return builder1;
    }

    private BiConsumer<DATA, FriendlyByteBuf> encoder;
    private Function<FriendlyByteBuf, DATA> decoder;

    private NetworkCodec(Class<? super DATA> packetType) {}

    public NetworkCodec<DATA> encoder(BiConsumer<DATA, FriendlyByteBuf> encoder) {
        this.encoder = encoder;
        return this;
    }

    public NetworkCodec<DATA> decoder(Function<FriendlyByteBuf, DATA> decoder) {
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
