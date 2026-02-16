package com.riverstone.unknown303.modmanager.data;

import com.riverstone.unknown303.modmanager.feature.AbstractModFeature;
import com.riverstone.unknown303.modmanager.global.Identifier;
import com.riverstone.unknown303.modmanager.feature.AbstractFeatureType;
import com.riverstone.unknown303.modmanager.global.User;
import com.riverstone.unknown303.modmanager.global.Users;
import com.riverstone.unknown303.modmanager.networking.packet.Packets;

import java.util.List;
import java.util.Map;

public class Registries {
    public static final Registry<AbstractFeatureType<?>> FEATURE_TYPES = new Registry<>(
            new Identifier(Users.SYSTEM, "feature_types"),
            Builders.FEATURE_TYPES_REGISTRY);
    public static final Registry<AbstractModFeature> FEATURES = new Registry<>(
            new Identifier(Users.SYSTEM, "features"), Builders.FEATURE_REGISTRY);

    public static class Builders {
        public static final DataBuilder<AbstractFeatureType<?>> FEATURE_TYPES =
                DataBuilder.register(new Identifier(Users.SYSTEM, "feature_types"),
                        new DataBuilder<AbstractFeatureType<?>>(AbstractFeatureType.class)
                                .encoder((type,
                                          networker) -> networker.writeUTF(
                                                  Registries.FEATURE_TYPES.getKey(type)))
                                .decoder(networker ->
                                        Registries.FEATURE_TYPES.getValue(
                                                Identifier.parse(networker.readUTF()))));
        public static final DataBuilder<Map.Entry<Identifier, AbstractFeatureType<?>>>
                FEATURE_TYPES_REGISTRY = DataBuilder.register(new Identifier(Users.SYSTEM,
                        "feature_types_database_builder"),
                DataBuilder.toDatabaseBuilder(FEATURE_TYPES));

        public static final DataBuilder<AbstractModFeature> FEATURES =
                DataBuilder.register(new Identifier(Users.SYSTEM, "features"),
                        DataBuilder.builder(AbstractModFeature.class)
                                .encoder((feature,
                                          networker) ->
                                        networker.writeJson(
                                                GsonHelper.fromObject(feature)))
                                .decoder(networker ->
                                        GsonHelper.toObject(networker.readJson(),
                                                AbstractModFeature.class)));
        public static final DataBuilder<Map.Entry<Identifier, AbstractModFeature>>
                FEATURE_REGISTRY = DataBuilder.register(new Identifier(Users.SYSTEM,
                "features_database_builder"),
                DataBuilder.toDatabaseBuilder(FEATURES));

        public static void register() {
            List.of(FEATURES, FEATURE_TYPES, FEATURE_REGISTRY, FEATURE_TYPES_REGISTRY);
        }
    }

    public static void register() {
        Builders.register();
        Packets.register();
    }
}