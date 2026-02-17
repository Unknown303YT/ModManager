package com.riverstone.unknown303.modmanager.common.networking.packet.status;

import com.riverstone.unknown303.modmanager.common.data.NetworkCodec;
import com.riverstone.unknown303.modmanager.common.networking.context.base.ClientPacketContext;
import com.riverstone.unknown303.modmanager.common.networking.packet.ClientboundPacket;

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

    @Override
    public void handle(ClientPacketContext context) {
        context.onStatus(this);
    }

    @Override
    public NetworkCodec<ClientboundStatusPacket> getCodec() {
        return null;
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
