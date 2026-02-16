package com.riverstone.unknown303.modmanager;

import com.riverstone.unknown303.modmanager.data.Registries;
import com.riverstone.unknown303.modmanager.global.Logger;
import com.riverstone.unknown303.modmanager.global.Users;
import com.riverstone.unknown303.modmanager.global.Util;
import com.riverstone.unknown303.modmanager.networking.Server;

import java.util.Scanner;

public class ServerMain {
    public static void main(String[] args) {
        Logger.enableDebugging();
        Users.register();
        Registries.register();
        Server.init();
        Server.startServer();

        Scanner input = new Scanner(System.in);
        Thread shutdownCheck = new Thread(() ->
                Util.onCommand(input, "stop", Server::stopServer));
        shutdownCheck.start();
    }
}
