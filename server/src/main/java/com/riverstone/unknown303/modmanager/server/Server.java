package com.riverstone.unknown303.modmanager.server;

import com.google.gson.JsonElement;
import com.riverstone.unknown303.modmanager.common.Constants;
import com.riverstone.unknown303.modmanager.common.data.GsonHelper;
import com.riverstone.unknown303.modmanager.common.global.Logger;
import com.riverstone.unknown303.modmanager.common.global.Util;
import com.riverstone.unknown303.modmanager.common.networking.packet.ClientboundPacket;
import com.riverstone.unknown303.modmanager.common.networking.netty.PacketDecoder;
import com.riverstone.unknown303.modmanager.common.networking.netty.PacketEncoder;
import com.riverstone.unknown303.modmanager.user.Users;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;

import javax.net.ssl.KeyManagerFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

public class Server {

    private static volatile boolean running = false;
    private static volatile int initCount = 0;
    private static Channel serverChannel;

    public static void startServer() {
        if (running) {
            Logger.getLogger().warn("Server is already running!");
            return;
        }

        Logger.getLogger().info("Starting server...");
        running = true;
    }

    public static void stopServer() {
        if (!running) {
            Logger.getLogger().warn("Server is not yet running!");
            return;
        }

        running = false;
    }

    public static void init() {
        if (initCount > 1)
            Logger.getLogger("Networking Thread").error(
                    "Server has already been initialized!");
        Thread thread = new Thread(Server::runServer);
        thread.start();
        initCount++;
    }

    private static void runServer() {
        try {
            while (!running)
                Thread.sleep(5000L);

            Logger.getLogger().info("Loading keystore...");
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(Server.class.getClassLoader().getResourceAsStream(
                    "serverkeystore.jks"),
                    Constants.KEYSTORE_PASSWORD.toCharArray());

            KeyManagerFactory keyManagerFactory =
                    KeyManagerFactory.getInstance("SunX509");
            keyManagerFactory.init(keyStore, Constants.KEYSTORE_PASSWORD.toCharArray());

            Logger.getLogger().info("Loading TLS...");
            SslContext sslContext = SslContextBuilder.forServer(keyManagerFactory).build();

            EventLoopGroup boss = new NioEventLoopGroup(1);
            EventLoopGroup worker = new NioEventLoopGroup();

            ServerBootstrap bootstrap = new ServerBootstrap()
                    .group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();

                            pipeline.addLast(sslContext.newHandler(socketChannel.alloc()));
                            pipeline.addLast(new PacketDecoder());
                            pipeline.addLast(new PacketEncoder());
                            pipeline.addLast(new ServerHandler());
                        }
                    });

            ChannelFuture future = bootstrap.bind(Constants.PORT).sync();
            serverChannel = future.channel();

            Logger.getLogger().info("Server Started!");

            Logger.getLogger().info("Saving...");
            save();
            Logger.getLogger().info("Saved!");
        } catch (InterruptedException | IOException | KeyStoreException |
                 CertificateException | NoSuchAlgorithmException |
                 UnrecoverableKeyException e) {
            Logger.getLogger("Networking Thread").fatal(e);
        }
    }

    public static void sendPacket(String clientAddress, ClientboundPacket<?> packet) {
        Channel channel = ServerConnections.CLIENTS.get(clientAddress);
        if (channel != null && channel.isActive())
            channel.writeAndFlush(packet);
        else
            Logger.getLogger("Networking Thread").error("Cannot send packet: client not connected " + clientAddress);
    }

    private static void save() throws IOException {
        File userOutput = new File(Util.getDataFolder().getAbsolutePath() +
                (Util.getDataFolder().getAbsolutePath().endsWith(File.pathSeparator) ?
                        "" : File.pathSeparator) + "users.json");
        userOutput.createNewFile();
        FileOutputStream userFileOutput = new FileOutputStream(userOutput);
        JsonElement usersJson = Users.USERS.encode();
        String jsonString = GsonHelper.toJsonString(usersJson);
        userFileOutput.write(jsonString.getBytes(StandardCharsets.UTF_8));
        userFileOutput.close();
    }
}
