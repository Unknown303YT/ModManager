package com.riverstone.unknown303.modmanager;

import com.riverstone.unknown303.modmanager.data.Registries;
import com.riverstone.unknown303.modmanager.global.Logger;
import com.riverstone.unknown303.modmanager.global.User;
import com.riverstone.unknown303.modmanager.global.Users;
import com.riverstone.unknown303.modmanager.global.Util;
import com.riverstone.unknown303.modmanager.networking.Client;
import com.riverstone.unknown303.modmanager.networking.Server;

import java.text.MessageFormat;
import java.util.Scanner;

public class ClientMain {

    public static void main(String[] args) {
        Logger.enableDebugging();
        Users.register();
        Registries.register();
        Logger.enableDebugging();
        Client.startClient();

        Runtime.getRuntime().addShutdownHook(new Thread(Client::stopClient));

        createUser();
    }

    private static void createUser() {
        Scanner input = new Scanner(System.in);
        System.out.println("Please enter the username:");
        String username = input.nextLine();
        System.out.println("Please enter the password:");
        System.out.println("TEST");
        String password = input.nextLine();
        Logger.getLogger().info("Creating user...");
        User user = User.onClient(username, password);
        Logger.getLogger().info(MessageFormat.format("USER CREATED! Username = " +
                "{0}, Hash = {1}, Salt = {2}, Token = {3}",
                user.getUsername(), user.getHash(), user.getSalt(), user.getToken()));
    }
}
