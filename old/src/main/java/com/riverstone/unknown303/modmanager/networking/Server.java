package com.riverstone.unknown303.modmanager.networking;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.riverstone.unknown303.modmanager.data.DataBuilder;
import com.riverstone.unknown303.modmanager.data.GsonHelper;
import com.riverstone.unknown303.modmanager.global.*;
import com.riverstone.unknown303.modmanager.networking.packet.ClientboundPacket;
import com.riverstone.unknown303.modmanager.networking.packet.ServerboundPacket;

import javax.net.ssl.*;
import java.io.*;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;

public class Server {
    public static final int PORT = 5000;
    public static final String KEYSTORE_PASSWORD = "pr0j3ctM@n@g3r";

    private static final Map<String, Map.Entry<DataOutputStream, DataInputStream>>
            CLIENT_STREAMS = new HashMap<>();

    private static volatile boolean running = false;
    private static volatile int initCount = 0;

    private static volatile int runningClients = 0;

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
        initCount = 0;
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
                    KEYSTORE_PASSWORD.toCharArray());

            KeyManagerFactory keyManagerFactory =
                    KeyManagerFactory.getInstance("SunX509");
            keyManagerFactory.init(keyStore, KEYSTORE_PASSWORD.toCharArray());

            Logger.getLogger().info("Loading TLS...");
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagerFactory.getKeyManagers(), null, null);

            SSLServerSocketFactory serverFactory = sslContext.getServerSocketFactory();
            SSLServerSocket server = (SSLServerSocket)
                    serverFactory.createServerSocket(PORT);

            Logger.getLogger().info("Server Started!");
            while (running) {
                SSLSocket client = (SSLSocket) server.accept();
                Logger.getLogger("Networking Thread")
                        .info("Connected to client %s! Attempting setup..."
                                .formatted(client.getInetAddress()));
                Thread clientThread = new Thread(() -> handleClient(client));
                clientThread.start();
            }

            while (runningClients > 0);
            server.close();
            Logger.getLogger("Networking Thread").info("Server closed.");

            Logger.getLogger().info("Saving...");
            save();
            Logger.getLogger().info("Saved!");
        } catch (InterruptedException | IOException | KeyStoreException |
                 CertificateException | NoSuchAlgorithmException |
                 UnrecoverableKeyException | KeyManagementException e) {
            runningClients = 0;
            Logger.getLogger("Networking Thread").fatal(e);
        }
    }

    public static <PACKET extends ClientboundPacket<PACKET>> String
            sendPacket(String clientAddress, PACKET packet) {
        if (!running)
            Logger.getLogger().error("Tried to send packet without being " +
                    "connected to the server!", IllegalStateException::new);
        runningClients++;
        Logger.getLogger().info("Sending packet " + packet.getPacketBuilder().getId() +
                " to client " + clientAddress);
        DataBuilder<PACKET> builder = packet.getPacketBuilder();
        JsonObject json = builder.build(packet).encode();
        AtomicReference<String> answer = new AtomicReference<>();
        withClient(clientAddress,
                (dataOutput, dataInput) -> {
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

                answer.set(response.replaceFirst("STATUS " , ""));

            } catch (UTFDataFormatException e) {
                Logger.getLogger("Client Handler Thread")
                        .warn("Requested UTF, got non-UTF data!", e);
            } catch (SocketException | SSLException | EOFException e) {
                runningClients--;
                CLIENT_STREAMS.remove(clientAddress);
                Logger.getLogger().warn("Client " + clientAddress +
                        " closed the connection!", e);
            } catch (IOException | InterruptedException e) {
                runningClients--;
                Logger.getLogger().fatal(e);
            }
        });

        runningClients--;
        Logger.getLogger().info("Sent packet " + packet.getPacketBuilder().getId() +
                " to client " + clientAddress + "!");
        return answer.get();
    }

    private static void withClient(String clientAddress,
                           BiConsumer<DataOutputStream, DataInputStream> action) {
        Map.Entry<DataOutputStream, DataInputStream> entry =
                CLIENT_STREAMS.get(clientAddress);
        action.accept(entry.getKey(), entry.getValue());
    }

    private static void handleClient(SSLSocket client) {
        Logger.getLogger().info("Client %s set up!"
                .formatted(client.getInetAddress().getHostAddress()));
        try {
            DataOutputStream dataOutput = new DataOutputStream(
                    new BufferedOutputStream(client.getOutputStream()));
            DataInputStream dataInput = new DataInputStream(
                    new BufferedInputStream(client.getInputStream()));
            if (CLIENT_STREAMS.containsKey(client.getInetAddress().getHostAddress()))
                Logger.getLogger("Client Handler Thread").fatal(
                        "Same INetAddress found in CLIENT_STREAMS! " +
                                "Client Address: " + client.getInetAddress(),
                        IllegalStateException::new);

            CLIENT_STREAMS.put(client.getInetAddress().getHostAddress(),
                    Map.entry(dataOutput, dataInput));

            runningClients++;
            receive(client.getInetAddress().getHostAddress(), client);
        } catch (SocketException | SSLException | EOFException e) {
            runningClients--;
            CLIENT_STREAMS.remove(client.getInetAddress().getHostAddress());
            Logger.getLogger().warn("Client " +
                    client.getInetAddress().getHostAddress() +
                    " closed the connection!", e);
        } catch (IOException e) {
            runningClients--;
            Logger.getLogger().fatal(e);
        }
    }

    @SuppressWarnings("unchecked")
    private static <PACKET extends ServerboundPacket<PACKET>> void
            receive(String clientAddress, SSLSocket client) {
        Logger.getLogger().info("Listening for packet from client %s..."
                .formatted(clientAddress));
        DataInputStream dataInput = CLIENT_STREAMS.get(clientAddress).getValue();
        DataOutputStream dataOutput = CLIENT_STREAMS.get(clientAddress).getKey();
        try {
            String command = dataInput.readUTF();
            while (!command.equals("PACKET")) {
                Thread.sleep(5000L);
                Logger.getLogger().info("Got String: " + command);
                command = dataInput.readUTF();
            }

            Logger.getLogger().info("Got String: " + command);
            runningClients++;
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
            PACKET packet = builder.build(FriendlyNetworker.reader(json));
            dataOutput.writeUTF("STATUS " + packet.handle(clientAddress));
            dataOutput.flush();
            Logger.getLogger().info("Received packet " + builderId + "!");
            runningClients--;

            if (running && client.isConnected() && !client.isClosed())
                receive(clientAddress, client);
            else {
                CLIENT_STREAMS.remove(clientAddress);
                runningClients--;
            }

        } catch (UTFDataFormatException e) {
            Logger.getLogger("Client Handler Thread")
                    .warn("Requested UTF, got non-UTF data!", e);
        } catch (SocketException | SSLException | EOFException e) {
            runningClients--;
            CLIENT_STREAMS.remove(clientAddress);
            Logger.getLogger().warn("Client " + clientAddress +
                    " closed the connection!", e);
        } catch (IOException | InterruptedException e) {
            runningClients--;
            Logger.getLogger().fatal(e);
        }
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
