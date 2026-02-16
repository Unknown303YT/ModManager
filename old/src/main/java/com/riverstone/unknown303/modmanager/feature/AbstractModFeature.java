package com.riverstone.unknown303.modmanager.feature;

import com.riverstone.unknown303.modmanager.global.Identifier;
import com.riverstone.unknown303.modmanager.data.Registries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractModFeature {
    private final AbstractFeatureType<?> type;
    private final List<String> description;

    public AbstractModFeature(AbstractFeatureType<?> type, String... description) {
        this(type, Arrays.asList(description));
    }

    public AbstractModFeature(AbstractFeatureType<?> type, List<String> description) {
        this.type = type;
        this.description = description;
    }

    public AbstractFeatureType<?> getType() {
        return type;
    }

    public List<String> getDescription() {
        return description;
    }

    public List<AbstractModFeature> dependents() {
        return new ArrayList<>();
    }

    public Identifier getId() {
        return Registries.FEATURES.getKey(this);
    }
}
