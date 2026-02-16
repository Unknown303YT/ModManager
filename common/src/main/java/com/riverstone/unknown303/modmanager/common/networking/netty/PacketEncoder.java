package com.riverstone.unknown303.modmanager.common.networking.netty;

import com.riverstone.unknown303.modmanager.common.data.NetworkCodec;
import com.riverstone.unknown303.modmanager.common.networking.FriendlyByteBuf;
import com.riverstone.unknown303.modmanager.common.networking.packet.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class PacketEncoder extends MessageToByteEncoder<Packet<?>> {
    @Override
    protected void encode(ChannelHandlerContext context, Packet<?> packet, ByteBuf byteBuf) throws Exception {
        FriendlyByteBuf buf = new FriendlyByteBuf(byteBuf);

        buf.writeByte(0x01);

        NetworkCodec<Packet<?>> builder = (NetworkCodec<Packet<?>>) packet.getCodec();

        buf.writeUtf(builder.getId().toString());

        buf.writeBytes(builder.build(packet));
    }
}
