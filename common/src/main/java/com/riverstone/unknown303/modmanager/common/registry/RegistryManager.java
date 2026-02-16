package com.riverstone.unknown303.modmanager.common.registry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class RegistryManager {
    private static final List<Registry<?>> REGISTRIES = new ArrayList<>();

    private RegistryManager() {}

    public static <T> Registry<T> register(Registry<T> registry) {
        REGISTRIES.add(registry);
        return registry;
    }

    public static List<Registry<?>> getAll() {
        return Collections.unmodifiableList(REGISTRIES);
    }

    public static void clearAll() {
        for (Registry<?> registry : REGISTRIES)
            registry.clear();
    }
}
