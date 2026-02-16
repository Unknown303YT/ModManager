package com.riverstone.unknown303.modmanager.common.global;

import java.io.File;
import java.util.*;
import java.util.function.Function;

public class Util {
    public static String getReadableSize(long sizeInBytes) {
        String size = sizeInBytes + "B";
        if (sizeInBytes >= 1250) {
            size = sizeInBytes / 1000 + "KB";

            if (sizeInBytes >= 1.1e6) {
                size = Math.floor((double) sizeInBytes / 1000) / 1000 + "MB";

                if (sizeInBytes >= 1.1e9) {
                    size = Math.floor((double) sizeInBytes / 1e6) / 1000 + "GB";
                }
            }
        }
        return size;
    }

    public static String getCondensedClassName(Class<?> clazz) {
        String packageName = clazz.getPackageName();
        StringBuilder shortenedPackageName = new StringBuilder();
        int count = 0;
        for (int i = 0; i < packageName.length(); i++) {
            char c = packageName.charAt(i);
            if (c == '.') {
                count = 0;
                shortenedPackageName.append(c);
                continue;
            }

            if (count <= 1)
                shortenedPackageName.append(c);

            count++;
        }

        return shortenedPackageName + "." + clazz.getSimpleName();
    }

    public static boolean anyAreTrue(Boolean... array) {
        return anyAreTrue(Arrays.asList(array));
    }

    public static <T> boolean anyAreTrue(Function<T, Boolean> toBool, T... array) {
        return anyAreTrue(Arrays.asList(array), toBool);
    }

    public static boolean anyAreTrue(List<Boolean> list) {
        for (boolean b : list) {
            if (b)
                return true;
        }

        return false;
    }

    public static <T> boolean anyAreTrue(List<T> list, Function<T, Boolean> toBool) {
        for (T value : list) {
            if (toBool.apply(value))
                return true;
        }

        return false;
    }

    public static boolean anyAreFalse(Boolean... array) {
        return anyAreFalse(Arrays.asList(array));
    }

    public static <T> boolean anyAreFalse(Function<T, Boolean> toBool, T... array) {
        return anyAreFalse(Arrays.asList(array), toBool);
    }

    public static boolean anyAreFalse(List<Boolean> list) {
        for (boolean b : list) {
            if (!b)
                return true;
        }

        return false;
    }

    public static <T> boolean anyAreFalse(List<T> list, Function<T, Boolean> toBool) {
        for (T value : list) {
            if (!toBool.apply(value))
                return true;
        }

        return false;
    }

    public static <K, V> Map<V, K> invertMap(Map<K, V> map) {
        Map<V, K> inverted = new HashMap<>();
        map.forEach((key, value) ->
                inverted.put(value, key));
        return inverted;
    }

    public static void onCommand(Scanner in, String command,
                                  Runnable action) {
        while (!in.hasNextLine());
        if (Objects.equals(in.nextLine(), command))
            action.run();
        else
            onCommand(in, command, action);
    }

    public static File getDataFolder() {
        String os = System.getProperty("os.name").toLowerCase();
        String userHome = System.getProperty("user.home");
        String folderName;

        if (os.contains("win"))
            folderName = System.getenv("AppData");
        else if (os.contains("mac"))
            folderName = userHome + "/Library/Application Support";
        else
            folderName = userHome + "/.local/share";

        folderName += (folderName.endsWith(File.pathSeparator) ? "" : File.pathSeparator)
                + "Unknown303_ProjectManager";
        return new File(folderName);
    }
}
