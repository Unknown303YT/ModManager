package com.riverstone.unknown303.modmanager.server.projects;

import com.riverstone.unknown303.modmanager.common.global.Identifier;
import com.riverstone.unknown303.modmanager.common.project.Project;
import com.riverstone.unknown303.modmanager.common.project.ProjectRole;
import com.riverstone.unknown303.modmanager.common.user.UserIdentity;
import com.riverstone.unknown303.modmanager.server.saving.SaveManagerServer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class ProjectStore {
    private final Map<Identifier, ProjectData> projects = new HashMap<>();
    private final SaveManagerServer saveManager = new SaveManagerServer();

    public ProjectStore() {
        loadAll();
    }

    public void loadAll() {
        projects.clear();
        for (Identifier id : saveManager.listProjectIds()) {
            ProjectData data = saveManager.loadProjectData(id);
            if (data != null)
                projects.put(id, data);
        }
    }

    public Collection<ProjectData> all() {
        return projects.values();
    }

    public ProjectData get(Identifier id) {
        return projects.get(id);
    }

    private String normalize(String displayName) {
        return displayName.toLowerCase()
                .replace(" ", "_")
                .replaceAll("[^a-z0-9._-]", "_");
    }

    public ProjectData create(UserIdentity owner, String displayName) {
        String path = normalize(displayName);
        Identifier id = new Identifier(owner, path);

        Project project = new Project(id);
        project.setDisplayName(displayName);

        ProjectData data = new ProjectData(project, owner.getId(),
                new HashMap<>(Map.of(owner.getId(), ProjectRole.OWNER)));

        saveManager.writeProject(project);
        saveManager.writeMembers(id, data.getMembers());

        projects.put(id, data);
        return data;
    }

    public boolean delete(Identifier id) {
        if (!projects.containsKey(id))
            return false;

        boolean ok = saveManager.deleteProject(id);
        if (ok)
            projects.remove(id);

        return ok;
    }
}
