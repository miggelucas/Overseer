package com.overseer;

import com.overseer.logging.Logger;
import org.junit.jupiter.api.Test;
import static com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemOut;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoggerTests {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Test
    void testLogInfo() throws Exception {
        String message = "This is an info message";
        String output = tapSystemOut(() -> Logger.logInfo(message));
        String expectedTimestamp = LocalDateTime.now().format(formatter);
        assertTrue(output.contains("INFO"));
        assertTrue(output.contains(message));
        assertTrue(output.contains(expectedTimestamp.substring(0, 16))); // Verifica até os minutos para evitar discrepância de segundos
    }

    @Test
    void testLogDebug() throws Exception {
        String message = "This is a debug message";
        String output = tapSystemOut(() -> Logger.logDebug(message));
        String expectedTimestamp = LocalDateTime.now().format(formatter);
        assertTrue(output.contains("DEBUG"));
        assertTrue(output.contains(message));
        assertTrue(output.contains(expectedTimestamp.substring(0, 16))); // Verifica até os minutos para evitar discrepância de segundos
    }

    @Test
    void testLogError() throws Exception {
        String message = "This is an error message";
        String output = tapSystemOut(() -> Logger.logError(message));
        String expectedTimestamp = LocalDateTime.now().format(formatter);
        assertTrue(output.contains("ERROR"));
        assertTrue(output.contains(message));
        assertTrue(output.contains(expectedTimestamp.substring(0, 16))); // Verifica até os minutos para evitar discrepância de segundos
    }
}