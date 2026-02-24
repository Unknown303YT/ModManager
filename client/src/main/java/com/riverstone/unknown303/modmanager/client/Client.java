package com.riverstone.unknown303.modmanager.client;

import com.riverstone.unknown303.modmanager.client.auth.AuthClient;
import com.riverstone.unknown303.modmanager.client.service.DeviceIdManager;
import com.riverstone.unknown303.modmanager.common.Constants;
import com.riverstone.unknown303.modmanager.common.global.Logger;
import com.riverstone.unknown303.modmanager.common.networking.netty.PacketDecoder;
import com.riverstone.unknown303.modmanager.common.networking.netty.PacketEncoder;
import com.riverstone.unknown303.modmanager.common.networking.packet.AuthenticatedPacket;
import com.riverstone.unknown303.modmanager.common.networking.packet.ServerboundPacket;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;

import javax.net.ssl.TrustManagerFactory;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class Client {
    public static final String SERVER_ADDRESS = "localhost";
    
    private static boolean running = false;
    private static Channel channel;

    public static void startClient() {
        try {
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(Client.class.getClassLoader().getResourceAsStream(
                    "clienttruststore.jks"),
                    Constants.KEYSTORE_PASSWORD.toCharArray());

            TrustManagerFactory trustManagerFactory =
                    TrustManagerFactory.getInstance("SunX509");
            trustManagerFactory.init(keyStore);

            SslContext sslContext = SslContextBuilder.forClient()
                    .trustManager(trustManagerFactory).build();

            EventLoopGroup group = new NioEventLoopGroup();

            Bootstrap bootstrap = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(sslContext.newHandler(socketChannel.alloc(), SERVER_ADDRESS, Constants.PORT));
                            pipeline.addLast(new PacketDecoder());
                            pipeline.addLast(new PacketEncoder());
                            pipeline.addLast(new ClientHandler());
                        }
                    });

            ChannelFuture future = bootstrap.connect(SERVER_ADDRESS, Constants.PORT).sync();
            channel = future.channel();

            Logger.getLogger("Networking Thread")
                    .info("Successfully connected to server %s:%s!".formatted(
                            SERVER_ADDRESS, Constants.PORT
                    ));

            running = true;
        } catch (KeyStoreException | CertificateException | IOException |
                 NoSuchAlgorithmException e) {
            Logger.getLogger().fatal(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static void stopClient() {
        running = false;
    }

    public static void sendPacket(ServerboundPacket<?> packet) {
        if (channel != null && channel.isActive()) {
            if (packet instanceof AuthenticatedPacket<?> authPacket) {
                authPacket.setToken(AuthClient.INSTANCE.getSession().getToken());
                authPacket.setDeviceId(DeviceIdManager.getDeviceId());
            }
            channel.writeAndFlush(packet);
        } else
            Logger.getLogger("Networking Thread").error("Client not connected");
    }
}
