package com.riverstone.unknown303.modmanager.user;

public class User {
    private final String username;
    private final String hash;
    private final String salt;
    private final String token;
    private final boolean isServerSide;

    public static User onServer(String username, String salt, String hash,
                                String token) {
        return new User(username, hash, salt, token, true);
    }

    public User(String username, String hash, String salt, String token,
                boolean isServerSide) {
        this.username = username;
        this.hash = hash;
        this.salt = salt;
        this.token = token;
        this.isServerSide = isServerSide;
    }

    public boolean isServerSide() {
        return isServerSide;
    }

    public String getUsername() {
        return username;
    }

    public String getHash() {
        return hash;
    }

    public String getSalt() {
        return salt;
    }

    public String getToken() {
        return token;
    }
}
