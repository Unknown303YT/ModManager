package com.riverstone.unknown303.modmanager.common.networking.context.base;

import com.riverstone.unknown303.modmanager.common.networking.packet.status.ClientboundStatusPacket;
import com.riverstone.unknown303.modmanager.common.project.Project;
import com.riverstone.unknown303.modmanager.common.user.UserIdentity;

import java.util.List;

public interface ClientPacketContext extends PacketContext {
    String serverAddress();

    void onStatus(ClientboundStatusPacket status);
    void onLoginSuccess(UserIdentity identity, String token);

    void onProjectList(List<Project> projects);
    void onProjectOpened(Project project);
}
