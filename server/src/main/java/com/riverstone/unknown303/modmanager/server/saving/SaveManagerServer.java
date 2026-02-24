package com.riverstone.unknown303.modmanager.server.saving;

import com.riverstone.unknown303.modmanager.common.data.saving.SaveManagerCommon;
import com.riverstone.unknown303.modmanager.common.global.Identifier;
import com.riverstone.unknown303.modmanager.common.global.Logger;
import com.riverstone.unknown303.modmanager.common.project.Project;
import com.riverstone.unknown303.modmanager.common.project.ProjectRole;
import com.riverstone.unknown303.modmanager.server.projects.ProjectData;
import com.riverstone.unknown303.modmanager.server.projects.ProjectMembers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class SaveManagerServer {
    private final SaveManagerCommon common;

    public SaveManagerServer() {
        common = new SaveManagerCommon();
    }

    private Path projectFolder(Identifier id) {
        return common.resolve("projects", id.toFileSafeString());
    }

    private Path projectJson(Identifier id) {
        return projectFolder(id).resolve("project.json");
    }

    private Path membersJson(Identifier id) {
        return projectFolder(id).resolve("members.json");
    }

    /* WRITING */
    public void writeProject(Project project) {
        try {
            common.writeJson(projectJson(project.getId()),
                    Map.of("id", project.getId(),
                            "displayName", project.getDisplayName()));
        } catch (IOException e) {
            Logger.getLogger().error("Exception encountered while writing project.json", e);
        }
    }

    public void writeMembers(Identifier id, ProjectMembers members) {
        Map<String, String> map = new HashMap<>();
        for (Map.Entry<UUID, ProjectRole> entry : members.getMembers().entrySet())
            map.put(entry.getKey().toString(), entry.getValue().name());

        try {
            common.writeJson(membersJson(id),
                    Map.of(
                            "ownerId", members.getOwnerId().toString(),
                            "members", map
                    ));
        } catch (IOException e) {
            Logger.getLogger().error("Exception encountered while writing members.json", e);
        }
    }

    /* READING */
    public ProjectData loadProjectData(Identifier id) {
        try {
            Map<?, ?> projectMap = common.readJson(projectJson(id), Map.class);
            Map<?, ?> membersMap = common.readJson(membersJson(id), Map.class);

            String displayName = (String) projectMap.get("displayName");

            Project project = new Project(id);
            project.setDisplayName(displayName);

            UUID ownerId = UUID.fromString((String) membersMap.get("ownerId"));

            Map<String, String> rawMembers = (Map<String, String>) membersMap.get("members");
            Map<UUID, ProjectRole> members = new HashMap<>();

            if (rawMembers != null)
                for (Map.Entry<String, String> entry : rawMembers.entrySet())
                    members.put(UUID.fromString(entry.getKey()), ProjectRole.valueOf(entry.getValue()));

            return new ProjectData(project, ownerId, members);
        } catch (IOException e) {
            Logger.getLogger().error("Exception encountered when loading project data", e);
            return null;
        }
    }

    public boolean deleteProject(Identifier id) {
        try {
            Path projectFolder = projectFolder(id);

            Files.walk(projectFolder)
                    .sorted(Comparator.reverseOrder())
                    .forEach(path -> path.toFile().delete());

            return true;
        } catch (IOException e) {
            Logger.getLogger().error("Exception encountered while deleting project", e);
            return false;
        }
    }

    public List<Identifier> listProjectIds() {
        List<Identifier> list = new ArrayList<>();

        Path projects = common.resolve("projects");
        try {
            common.ensureFolder(projects);

            for (Path path : Files.list(projects).toList()) {
                if (!Files.isDirectory(path))
                    continue;
                Map<?, ?> projectMap = common.readJson(
                        path.resolve("project.json"), Map.class);
                list.add(Identifier.parse((String) projectMap.get("id")));
            }
        } catch (IOException e) {
            Logger.getLogger().error("Error encountered while finding projects", e);
        }

        return list;
    }
}
