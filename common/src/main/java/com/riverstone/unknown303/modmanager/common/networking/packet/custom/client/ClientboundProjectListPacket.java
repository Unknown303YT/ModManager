package com.riverstone.unknown303.modmanager.common.networking.packet.custom.client;

import com.riverstone.unknown303.modmanager.common.data.NetworkCodec;
import com.riverstone.unknown303.modmanager.common.networking.FriendlyByteBuf;
import com.riverstone.unknown303.modmanager.common.networking.context.base.ClientPacketContext;
import com.riverstone.unknown303.modmanager.common.networking.packet.ClientboundPacket;
import com.riverstone.unknown303.modmanager.common.networking.packet.Packets;
import com.riverstone.unknown303.modmanager.common.project.Project;

import java.util.ArrayList;
import java.util.List;

public final class ClientboundProjectListPacket extends ClientboundPacket<ClientboundProjectListPacket> {
    private final List<Project> projects;

    public ClientboundProjectListPacket(List<Project> projects) {
        this.projects = projects;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(projects.size());
        for (Project project : projects) {
            buf.writeIdentifier(project.getId());
            buf.writeUtf(project.getDisplayName());
        }
    }

    public static ClientboundProjectListPacket decode(FriendlyByteBuf buf) {
        List<Project> projects = new ArrayList<>();
        for (int i = 0; i < buf.readVarInt(); i++) {
            Project project = new Project(buf.readIdentifier());
            project.setDisplayName(buf.readUtf());
            projects.add(project);
        }

        return new ClientboundProjectListPacket(projects);
    }

    @Override
    public void handle(ClientPacketContext context) {
        context.projects().onProjectList(projects);
    }

    @Override
    public NetworkCodec<ClientboundProjectListPacket> getCodec() {
        return Packets.CLIENTBOUND_PROJECT_LIST_PACKET;
    }

    public List<Project> getProjects() {
        return projects;
    }
}
