package com.riverstone.unknown303.modmanager.client;

import com.riverstone.unknown303.modmanager.client.networking.context.ClientPacketHandlerImpl;
import com.riverstone.unknown303.modmanager.common.global.Logger;
import com.riverstone.unknown303.modmanager.common.networking.packet.ClientboundPacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientHandler extends SimpleChannelInboundHandler<ClientboundPacket<?>> {
    @Override
    protected void channelRead0(ChannelHandlerContext context, ClientboundPacket<?> packet) throws Exception {
        String status = packet.handle(new ClientPacketHandlerImpl(context.channel().remoteAddress().toString()));
    }

    @Override
    public void channelInactive(ChannelHandlerContext context) throws Exception {
        Logger.getLogger("Networking Thread").info("Disconnected from server: " + context.channel().remoteAddress());
        super.channelInactive(context);
    }
}
