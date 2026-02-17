package com.riverstone.unknown303.modmanager.common.global;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;

public class CryptoUtil {
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final Argon2 ARGON_2 = Argon2Factory.create(
            Argon2Factory.Argon2Types.ARGON2id
    );

    private static final int ITERATIONS = 1;
    private static final int MEMORY_KIB = 46 * 1024;
    private static final int PARALLELISM = 1;

    public static String hashPassword(String password) {
        char[] chars = password.toCharArray();
        try {
            return ARGON_2.hash(ITERATIONS, MEMORY_KIB, PARALLELISM, chars);
        } finally {
            wipe(chars);
        }
    }

    public static boolean verifyPassword(String password, String storedHash) {
        char[] chars = password.toCharArray();
        try {
            return ARGON_2.verify(storedHash, chars);
        } finally {
            wipe(chars);
        }
    }

    private static void wipe(char[] array) {
        Arrays.fill(array, (char) 0);
    }

    public static String generateToken() {
        byte[] token = new byte[32];
        RANDOM.nextBytes(token);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(token);
    }

    public static String hashToken(String token) {
        try {
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            byte[] hash = sha256.digest(token.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            Logger.getLogger().fatal("SHA-256 not available", e);
        }
        return null;
    }
}
