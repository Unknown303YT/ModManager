package com.riverstone.unknown303.modmanager.common.networking.netty;

import com.riverstone.unknown303.modmanager.common.data.NetworkCodec;
import com.riverstone.unknown303.modmanager.common.global.Identifier;
import com.riverstone.unknown303.modmanager.common.global.Logger;
import com.riverstone.unknown303.modmanager.common.networking.FriendlyByteBuf;
import com.riverstone.unknown303.modmanager.common.networking.packet.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;
import java.util.UUID;

public class PacketDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext context, ByteBuf byteBuf, List<Object> list) throws Exception {
        FriendlyByteBuf buf = new FriendlyByteBuf(byteBuf);

        if (!buf.isReadable())
            return;

        byte opcode = buf.readByte();
        if (opcode != 0x01)
            return;

        UUID requestId = buf.readUUID();

        Identifier id = Identifier.parse(buf.readUtf());

        NetworkCodec<Packet<?>> builder = (NetworkCodec<Packet<?>>) NetworkCodec.BUILDERS.get(id);
        if (builder == null) {
            Logger.getLogger().error("Unknown packet ID: " + id);
            return;
        }

        Packet<?> packet = builder.build(new FriendlyByteBuf(buf.readBytes(Unpooled.buffer())));
        packet.setRequestId(requestId);
        list.add(packet);
    }
}
