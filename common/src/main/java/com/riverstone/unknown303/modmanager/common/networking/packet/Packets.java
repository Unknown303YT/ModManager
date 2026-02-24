package com.riverstone.unknown303.modmanager.common.networking.packet;

import com.riverstone.unknown303.modmanager.common.data.NetworkCodec;
import com.riverstone.unknown303.modmanager.common.global.Identifier;
import com.riverstone.unknown303.modmanager.common.networking.packet.custom.client.ClientboundLoginSuccessPacket;
import com.riverstone.unknown303.modmanager.common.networking.packet.custom.client.ClientboundProjectListPacket;
import com.riverstone.unknown303.modmanager.common.networking.packet.custom.client.ClientboundProjectOpenedPacket;
import com.riverstone.unknown303.modmanager.common.networking.packet.custom.server.ServerboundCreateAccountPacket;
import com.riverstone.unknown303.modmanager.common.networking.packet.custom.server.ServerboundLoginPacket;
import com.riverstone.unknown303.modmanager.common.networking.packet.custom.server.project.ServerboundCreateProjectPacket;
import com.riverstone.unknown303.modmanager.common.networking.packet.custom.server.project.ServerboundDeleteProjectPacket;
import com.riverstone.unknown303.modmanager.common.networking.packet.custom.server.project.ServerboundGetProjectsPacket;
import com.riverstone.unknown303.modmanager.common.networking.packet.custom.server.project.ServerboundOpenProjectPacket;
import com.riverstone.unknown303.modmanager.common.networking.packet.status.ClientboundStatusPacket;
import com.riverstone.unknown303.modmanager.common.user.Users;

public class Packets {
    public static final NetworkCodec<ClientboundStatusPacket> STATUS_PACKET =
            NetworkCodec.builder(ClientboundStatusPacket.class)
                    .encoder(ClientboundStatusPacket::encode)
                    .decoder(ClientboundStatusPacket::decode);
    public static final NetworkCodec<ClientboundLoginSuccessPacket> CLIENTBOUND_LOGIN_SUCCESS_PACKET =
            NetworkCodec.builder(ClientboundLoginSuccessPacket.class)
                    .encoder(ClientboundLoginSuccessPacket::encode)
                    .decoder(ClientboundLoginSuccessPacket::decode);
    public static final NetworkCodec<ClientboundProjectListPacket> CLIENTBOUND_PROJECT_LIST_PACKET =
            NetworkCodec.builder(ClientboundProjectListPacket.class)
                    .encoder(ClientboundProjectListPacket::encode)
                    .decoder(ClientboundProjectListPacket::decode);
    public static final NetworkCodec<ClientboundProjectOpenedPacket> CLIENTBOUND_PROJECT_OPENED_PACKET =
            NetworkCodec.builder(ClientboundProjectOpenedPacket.class)
                    .encoder(ClientboundProjectOpenedPacket::encode)
                    .decoder(ClientboundProjectOpenedPacket::decode);

    public static final NetworkCodec<ServerboundCreateProjectPacket> SERVERBOUND_CREATE_PROJECT_PACKET =
            NetworkCodec.builder(ServerboundCreateProjectPacket.class)
                    .encoder(ServerboundCreateProjectPacket::encode)
                    .decoder(ServerboundCreateProjectPacket::decode);
    public static final NetworkCodec<ServerboundDeleteProjectPacket> SERVERBOUND_DELETE_PROJECT_PACKET =
            NetworkCodec.builder(ServerboundDeleteProjectPacket.class)
                    .encoder(ServerboundDeleteProjectPacket::encode)
                    .decoder(ServerboundDeleteProjectPacket::decode);
    public static final NetworkCodec<ServerboundGetProjectsPacket> SERVERBOUND_GET_PROJECTS_PACKET =
            NetworkCodec.builder(ServerboundGetProjectsPacket.class)
                    .encoder(ServerboundGetProjectsPacket::encode)
                    .decoder(ServerboundGetProjectsPacket::decode);
    public static final NetworkCodec<ServerboundOpenProjectPacket> SERVERBOUND_OPEN_PROJECT_PACKET =
            NetworkCodec.builder(ServerboundOpenProjectPacket.class)
                    .encoder(ServerboundOpenProjectPacket::encode)
                    .decoder(ServerboundOpenProjectPacket::decode);
    public static final NetworkCodec<ServerboundCreateAccountPacket> SERVERBOUND_CREATE_ACCOUNT_PACKET =
            NetworkCodec.builder(ServerboundCreateAccountPacket.class)
                    .encoder(ServerboundCreateAccountPacket::encode)
                    .decoder(ServerboundCreateAccountPacket::decode);
    public static final NetworkCodec<ServerboundLoginPacket> SERVERBOUND_LOGIN_PACKET =
            NetworkCodec.builder(ServerboundLoginPacket.class)
                    .encoder(ServerboundLoginPacket::encode)
                    .decoder(ServerboundLoginPacket::decode);

    public static void register() {
        NetworkCodec.register(new Identifier(Users.SYSTEM, "status_packet"),
                STATUS_PACKET);
        NetworkCodec.register(new Identifier(Users.SYSTEM, "clientbound_login_success_packet"),
                CLIENTBOUND_LOGIN_SUCCESS_PACKET);
        NetworkCodec.register(new Identifier(Users.SYSTEM, "clientbound_project_list_packet"),
                CLIENTBOUND_PROJECT_LIST_PACKET);
        NetworkCodec.register(new Identifier(Users.SYSTEM, "clientbound_project_opened_packet"),
                CLIENTBOUND_PROJECT_OPENED_PACKET);

        NetworkCodec.register(new Identifier(Users.SYSTEM, "serverbound_create_project_packet"),
                SERVERBOUND_CREATE_PROJECT_PACKET);
        NetworkCodec.register(new Identifier(Users.SYSTEM, "serverbound_delete_project_packet"),
                SERVERBOUND_DELETE_PROJECT_PACKET);
        NetworkCodec.register(new Identifier(Users.SYSTEM, "serverbound_get_projects_packet"),
                SERVERBOUND_GET_PROJECTS_PACKET);
        NetworkCodec.register(new Identifier(Users.SYSTEM, "serverbound_open_project_packet"),
                SERVERBOUND_OPEN_PROJECT_PACKET);
        NetworkCodec.register(new Identifier(Users.SYSTEM, "serverbound_create_account_packet"),
                SERVERBOUND_CREATE_ACCOUNT_PACKET);
        NetworkCodec.register(new Identifier(Users.SYSTEM, "serverbound_login_packet"),
                SERVERBOUND_LOGIN_PACKET);
    }
}
