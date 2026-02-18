package com.riverstone.unknown303.modmanager.common.project;

import com.riverstone.unknown303.modmanager.common.global.Identifier;

public class Project {
    private final Identifier id;
    private String displayName;

    public Project(Identifier id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
