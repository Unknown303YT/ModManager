package com.riverstone.unknown303.modmanager.server;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerConnections {
    public static final Map<String, Channel> CLIENTS = new ConcurrentHashMap<>();
}
