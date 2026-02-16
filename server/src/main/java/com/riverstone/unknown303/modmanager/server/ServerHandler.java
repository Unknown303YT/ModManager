package com.riverstone.unknown303.modmanager.server;

import com.riverstone.unknown303.modmanager.global.Logger;
import com.riverstone.unknown303.modmanager.networking.packet.ServerboundPacket;
import com.riverstone.unknown303.modmanager.networking.packet.netty.StatusPacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ServerHandler extends SimpleChannelInboundHandler<ServerboundPacket<?>> {
    @Override
    protected void channelRead0(ChannelHandlerContext context, ServerboundPacket<?> packet) throws Exception {
        String client = context.channel().remoteAddress().toString();
        String status = packet.handle(client);

        context.writeAndFlush(new StatusPacket(status));
    }

    @Override
    public void channelActive(ChannelHandlerContext context) throws Exception {
        super.channelActive(context);
        String address = context.channel().remoteAddress().toString();
        ServerConnections.CLIENTS.put(address, context.channel());
        Logger.getLogger("Networking Thread").info("Client Connected: " + address);
    }

    @Override
    public void channelInactive(ChannelHandlerContext context) throws Exception {
        String address = context.channel().remoteAddress().toString();
        ServerConnections.CLIENTS.remove(address);
        Logger.getLogger("Networking Thread").info("Client Disconnected: " + address);
        super.channelInactive(context);
    }
}
