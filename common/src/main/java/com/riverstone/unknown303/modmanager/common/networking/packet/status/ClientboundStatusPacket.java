package com.riverstone.unknown303.modmanager.common.networking.packet.status;

import com.riverstone.unknown303.modmanager.common.data.NetworkCodec;
import com.riverstone.unknown303.modmanager.common.networking.FriendlyByteBuf;
import com.riverstone.unknown303.modmanager.common.networking.context.base.ClientPacketContext;
import com.riverstone.unknown303.modmanager.common.networking.packet.ClientboundPacket;
import com.riverstone.unknown303.modmanager.common.networking.packet.Packets;

import java.util.Map;
import java.util.UUID;

public class ClientboundStatusPacket extends ClientboundPacket<ClientboundStatusPacket> {
    private final StatusType type;
    private final StatusCode code;
    private final String message;
    private final UUID respondingTo;
    private final Map<String, String> metadata;

    public ClientboundStatusPacket(StatusType type, StatusCode code, String message, UUID respondingTo, Map<String, String> metadata) {
        this.type = type;
        this.code = code;
        this.message = message;
        this.respondingTo = respondingTo;
        this.metadata = metadata;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeEnum(type);
        buf.writeEnum(code);
        buf.writeUtf(message);
        buf.writeUUID(respondingTo);
        buf.writeMap(metadata, FriendlyByteBuf::writeUtf, FriendlyByteBuf::writeUtf);
    }

    public static ClientboundStatusPacket decode(FriendlyByteBuf buf) {
        return new ClientboundStatusPacket(
                buf.readEnum(StatusType.class),
                buf.readEnum(StatusCode.class),
                buf.readUtf(),
                buf.readUUID(),
                buf.readMap(FriendlyByteBuf::readUtf, FriendlyByteBuf::readUtf));
    }

    @Override
    public void handle(ClientPacketContext context) {
        context.onStatus(this);
    }

    @Override
    public NetworkCodec<ClientboundStatusPacket> getCodec() {
        return Packets.STATUS_PACKET;
    }

    public StatusType type() {
        return type;
    }

    public StatusCode code() {
        return code;
    }

    public String message() {
        return message;
    }

    public UUID respondingTo() {
        return respondingTo;
    }

    public Map<String, String> metadata() {
        return metadata;
    }
}
