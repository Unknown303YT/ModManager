package com.riverstone.unknown303.modmanager.client;

import com.riverstone.unknown303.modmanager.global.Logger;
import com.riverstone.unknown303.modmanager.networking.packet.ClientboundPacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientHandler extends SimpleChannelInboundHandler<ClientboundPacket<?>> {
    @Override
    protected void channelRead0(ChannelHandlerContext context, ClientboundPacket<?> packet) throws Exception {
        packet.handle(context.channel().remoteAddress().toString());
    }

    @Override
    public void channelInactive(ChannelHandlerContext context) throws Exception {
        Logger.getLogger("Networking Thread").info("Disconnected from server: " + context.channel().remoteAddress());
        super.channelInactive(context);
    }
}
