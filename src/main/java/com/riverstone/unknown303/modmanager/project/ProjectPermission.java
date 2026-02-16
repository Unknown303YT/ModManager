package com.riverstone.unknown303.modmanager.project;

import com.google.gson.JsonObject;
import com.riverstone.unknown303.modmanager.data.SaveCodec;

public enum ProjectPermission {
    MANAGE_PROJECT,
    MANAGE_MEMBERS,
    EDIT_CONTENT,
    DELETE_CONTENT,
    VIEW_CONTENT;

//    public static final SaveCodec<ProjectPermission> SAVE_CODEC =
//            SaveCodec.builder(ProjectPermission.class)
//                    .encoder(((permission, json) -> {
//                        json.addProperty("permission", switch (permission) {
//                            case MANAGE_PROJECT -> "MANAGE_PROJECT";
//                            case MANAGE_MEMBERS -> "MANAGE_MEMBERS";
//                            case EDIT_CONTENT -> "EDIT_CONTENT";
//                            case DELETE_CONTENT -> "DELETE_CONTENT";
//                            case VIEW_CONTENT -> "VIEW_CONTENT";
//                        });
//                    }))
//                    .decoder(json ->
//                            switch (json.get("permission").getAsString()) {
//                        case "MANAGE_PROJECT" -> MANAGE_PROJECT;
//                        case "MANAGE_MEMBERS" -> MANAGE_MEMBERS;
//                        case "EDIT_CONTENT" -> EDIT_CONTENT;
//                        case "DELETE_CONTENT" -> DELETE_CONTENT;
//                        case "VIEW_CONTENT" -> VIEW_CONTENT;
//                        default -> {
//                            IllegalStateException e = new IllegalStateException("Unexpected value: " + json.get("permission").getAsString());
//                            Logger.getLogger().error(e);
//                            yield null;
//                        }
//                    });
}
