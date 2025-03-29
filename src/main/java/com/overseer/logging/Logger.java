package com.overseer.logging;

import com.overseer.util.Level;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void log(Level level, String message) {
        String timestamp = LocalDateTime.now().format(formatter);
        System.out.printf("%s [%s] %s%n", timestamp, level, message);
    }

    public static void logInfo(String message) {
        log(Level.INFO, message);
    }

    public static void logDebug(String message) {
        log(Level.DEBUG, message);
    }

    public static void logError(String message) {
        log(Level.ERROR, message);
    }
}
