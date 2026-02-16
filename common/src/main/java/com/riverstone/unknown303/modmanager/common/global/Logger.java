package com.riverstone.unknown303.modmanager.common.global;

import java.time.LocalTime;
import java.util.function.Function;

public class Logger {
    private static final StackWalker STACK_WALKER =
            StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);

    private static boolean shouldDebug = false;

    private final Class<?> ownerClass;
    private final String threadPrefix;

    private Logger(Class<?> ownerClass) {
        this(ownerClass, null);
    }

    private Logger(Class<?> ownerClass, String threadPrefix) {
        this.ownerClass = ownerClass;
        this.threadPrefix = threadPrefix;
    }


    public void info(String message) {
        String midPrefix = threadPrefix == null ? "INFO" :
                threadPrefix + "/INFO";
        System.out.printf("%s [%s] %s: %s%n",
                timePrefix(), midPrefix, classPrefix(), message);
    }
    public void debug(String message) {
        if (shouldDebug) {
            String midPrefix = threadPrefix == null ? "DEBUG" :
                    threadPrefix + "/DEBUG";
            System.out.printf("%s [%s] %s: %s%n",
                    timePrefix(), midPrefix, classPrefix(), message);
        }
    }


    public void warn(Throwable throwable) {
        warn(throwable.getMessage(), throwable);
    }

    public void warn(String message) {
        String midPrefix = threadPrefix == null ? "WARN" :
                threadPrefix + "/WARN";
        System.err.printf("%s [%s] %s: %s%n",
                timePrefix(), midPrefix, classPrefix(), message);
    }

    public void warn(String message, Throwable throwable) {
        String midPrefix = threadPrefix == null ? "WARN" :
                threadPrefix + "/WARN";
        System.err.printf("%s [%s] %s: %s%n",
                timePrefix(), midPrefix, classPrefix(), message +
                        "\nThrowable Error: " + throwable.toString());
        throwable.printStackTrace(System.err);
    }

    public void warn(String message, Function<String, Throwable> toThrowable) {
        warn(message, toThrowable.apply(message));
    }

    public void error(Throwable throwable) {
        error(throwable.getMessage(), throwable);
    }

    public void error(String message) {
        String midPrefix = threadPrefix == null ? "ERROR" :
                threadPrefix + "/ERROR";
        System.err.printf("%s [%s] %s: %s%n",
                timePrefix(), midPrefix, classPrefix(), message);
    }

    public void error(String message, Throwable throwable) {
        String midPrefix = threadPrefix == null ? "ERROR" :
                threadPrefix + "/ERROR";
        System.err.printf("%s [%s] %s: %s%n",
                timePrefix(), midPrefix, classPrefix(), message +
                        "\nThrowable Error: " + throwable.toString());
        throwable.printStackTrace(System.err);
    }

    public void error(String message, Function<String, Throwable> toThrowable) {
        error(message, toThrowable.apply(message));
    }

    public void fatal(Throwable throwable) {
        fatal(throwable.getMessage(), throwable);
    }

    public void fatal(String message) {
        String midPrefix = threadPrefix == null ? "FATAL" :
                threadPrefix + "/FATAL";
        System.err.printf("%s [%s] %s: %s%n",
                timePrefix(), midPrefix, classPrefix(), message);
        throw new RuntimeException(message);
    }

    public void fatal(String message, Throwable throwable) {
        String midPrefix = threadPrefix == null ? "FATAL" :
                threadPrefix + "/FATAL";
        System.err.printf("%s [%s] %s: %s%n",
                timePrefix(), midPrefix, classPrefix(), message +
                        "\nThrowable Error: " + throwable.toString());
        throwable.printStackTrace(System.err);
        throw new RuntimeException(throwable);
    }

    public void fatal(String message, Function<String, Throwable> toThrowable) {
        fatal(message, toThrowable.apply(message));
    }

    private String timePrefix() {
        LocalTime now = LocalTime.now();
        return "[%s:%s:%s]".formatted(now.getHour(), now.getMinute(),
                now.getSecond());
    }

    private String classPrefix() {
        return "[%s]".formatted(Util.getCondensedClassName(ownerClass));
    }

    public static Logger getLogger() {
        return new Logger(STACK_WALKER.getCallerClass());
    }

    public static Logger getLogger(String threadPrefix) {
        return new Logger(STACK_WALKER.getCallerClass(), threadPrefix);
    }

    public static void enableDebugging() {
        shouldDebug = true;
    }
}