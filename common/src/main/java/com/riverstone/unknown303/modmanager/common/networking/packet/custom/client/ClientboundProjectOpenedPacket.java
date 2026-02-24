package com.riverstone.unknown303.modmanager.common.networking.packet.custom.client;

import com.riverstone.unknown303.modmanager.common.data.NetworkCodec;
import com.riverstone.unknown303.modmanager.common.networking.FriendlyByteBuf;
import com.riverstone.unknown303.modmanager.common.networking.context.base.ClientPacketContext;
import com.riverstone.unknown303.modmanager.common.networking.packet.ClientboundPacket;
import com.riverstone.unknown303.modmanager.common.networking.packet.Packets;
import com.riverstone.unknown303.modmanager.common.project.Project;

public class ClientboundProjectOpenedPacket extends ClientboundPacket<ClientboundProjectOpenedPacket> {
    private final Project project;

    public ClientboundProjectOpenedPacket(Project project) {
        this.project = project;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeIdentifier(project.getId());
        buf.writeUtf(project.getDisplayName());
    }

    public static ClientboundProjectOpenedPacket decode(FriendlyByteBuf buf) {
        Project project = new Project(buf.readIdentifier());
        project.setDisplayName(buf.readUtf());
        return new ClientboundProjectOpenedPacket(project);
    }

    public Project getProject() {
        return project;
    }

    @Override
    public void handle(ClientPacketContext context) {
        context.projects().onProjectOpened(project);
    }

    @Override
    public NetworkCodec<ClientboundProjectOpenedPacket> getCodec() {
        return Packets.CLIENTBOUND_PROJECT_OPENED_PACKET;
    }
}
