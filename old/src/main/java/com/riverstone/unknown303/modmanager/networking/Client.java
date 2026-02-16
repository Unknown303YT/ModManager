package com.riverstone.unknown303.modmanager.networking;

import com.google.gson.JsonObject;
import com.riverstone.unknown303.modmanager.data.DataBuilder;
import com.riverstone.unknown303.modmanager.data.GsonHelper;
import com.riverstone.unknown303.modmanager.global.Identifier;
import com.riverstone.unknown303.modmanager.global.Logger;
import com.riverstone.unknown303.modmanager.networking.packet.ClientboundPacket;
import com.riverstone.unknown303.modmanager.networking.packet.ServerboundPacket;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.*;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;

public class Client {
    public static final String SERVER_ADDRESS = "localhost";
    
    private static boolean running = false;

    private static volatile DataOutputStream dataOutput = null;
    private static volatile DataInputStream dataInput = null;

    private static volatile SSLSocket server = null;

    public static void startClient() {
        try {
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(Client.class.getClassLoader().getResourceAsStream(
                    "clienttruststore.jks"),
                    Server.KEYSTORE_PASSWORD.toCharArray());

            TrustManagerFactory trustManagerFactory =
                    TrustManagerFactory.getInstance("SunX509");
            trustManagerFactory.init(keyStore);

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagerFactory.getTrustManagers(),
                    new SecureRandom());

            SSLSocketFactory factory = sslContext.getSocketFactory();
            server = (SSLSocket) factory.createSocket(SERVER_ADDRESS, Server.PORT);
            Logger.getLogger("Networking Thread")
                    .info("Successfully connected to server %s:%s!".formatted(
                            server.getInetAddress(), Server.PORT
                    ));
            running = true;
            Thread serverThread = new Thread(() -> handleServer(server));
            serverThread.start();
        } catch (KeyStoreException | CertificateException | IOException |
                 NoSuchAlgorithmException | KeyManagementException e) {
            Logger.getLogger().fatal(e);
        }
    }
    
    public static void stopClient() {
        running = false;
    }

    public static <PACKET extends ServerboundPacket<PACKET>> String sendPacket(PACKET packet) {
        if (!running || server.isClosed() || !server.isConnected())
            Logger.getLogger().error("Tried to send packet without being " +
                    "connected to the server!", IllegalStateException::new);
        Logger.getLogger().info("Sending packet " + packet.getPacketBuilder().getId());
        DataBuilder<PACKET> builder = packet.getPacketBuilder();
        JsonObject json = builder.build(packet).encode();
        AtomicReference<String> answer = new AtomicReference<>();
        try {
            dataOutput.writeUTF("PACKET");
            dataOutput.writeUTF(builder.getId().toString());
            dataOutput.writeUTF(GsonHelper.toJsonString(json));
            dataOutput.flush();

            String response = dataInput.readUTF();
            while (!response.startsWith("STATUS")) {
                Thread.sleep(5000L);
                response = dataInput.readUTF();
            }

            answer.set(response.replaceFirst("STATUS ", ""));
        } catch (UTFDataFormatException e) {
            Logger.getLogger("Server Handler Thread")
                    .warn("Requested UTF, got non-UTF data!", e);
        } catch (IOException | InterruptedException e) {
            Logger.getLogger().fatal(e);
        }

        Logger.getLogger().info("Sent packet " + packet.getPacketBuilder().getId() +
                "!");
        return answer.get();
    }

    private static void handleServer(SSLSocket server) {
        try {
            dataOutput = new DataOutputStream(new BufferedOutputStream(
                    server.getOutputStream()));
            dataInput = new DataInputStream(new BufferedInputStream(
                    server.getInputStream()));

            Thread receiver = new Thread(() -> receive(server));
            receiver.start();
        } catch (IOException e) {
            Logger.getLogger().fatal(e);
        }
    }

    @SuppressWarnings("unchecked")
    private static <PACKET extends ClientboundPacket<PACKET>>
            void receive(SSLSocket server) {
        Logger.getLogger().info("Listening for packet from server...");
        try {
            String command = dataInput.readUTF();
            while (!command.equals("PACKET")) {
                Thread.sleep(5000L);
                Logger.getLogger().info("Got String: " + command);
                command = dataInput.readUTF();
            }

            Logger.getLogger().info("Got String: " + command);
            String builderId = dataInput.readUTF();
            Logger.getLogger().info("Got String: " + builderId);
            Logger.getLogger().info("Receiving packet " + builderId + "...");
            DataBuilder<PACKET> builder = (DataBuilder<PACKET>)
                    DataBuilder.BUILDERS.get(
                            Identifier.parse(builderId));
            if (builder == null)
                Logger.getLogger("Client Handler Thread").fatal(
                        "Provided id does not match a PacketBuilder!",
                        IllegalStateException::new);
            String data = dataInput.readUTF();
            Logger.getLogger().info("Got JSON Data:\n" + data);
            JsonObject json = GsonHelper.fromJsonString(data).getAsJsonObject();
            PACKET packet =
                    builder.build(FriendlyNetworker.reader(json));
            dataOutput.writeUTF("STATUS " +
                    packet.handle(server.getLocalAddress().getHostAddress()));
            dataOutput.flush();
            Logger.getLogger().info("Received packet " + builderId + "!");

            if (running && server.isConnected() && !server.isClosed())
                receive(server);
            else {
                dataOutput = null;
                dataInput = null;
            }
            
        } catch (UTFDataFormatException e) {
            Logger.getLogger("Server Handler Thread")
                    .warn("Requested UTF, got non-UTF data!", e);
        } catch (IOException | InterruptedException e) {
            Logger.getLogger().fatal(e);
        }
    }
}
