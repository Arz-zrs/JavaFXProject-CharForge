package com.project.charforge.utils;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.LogRecord;
import java.util.Date;

public final class Logs {
    // Get App Name
    private static final Logger LOGGER = Logger.getLogger("CharForge");

    static {
        // Logs Formatting: [DATE TIME] [LEVEL] Message
        LOGGER.setUseParentHandlers(false);

        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new SimpleFormatter() {
            private static final String format = "[%1$tF %1$tT] [%2$-7s] %3$s %n";

            @Override
            public synchronized String format(LogRecord lr) {
                return String.format(format,
                        new Date(lr.getMillis()),
                        lr.getLevel().getLocalizedName(),
                        lr.getMessage()
                );
            }
        });

        LOGGER.addHandler(handler);
        LOGGER.setLevel(Level.ALL);
    }

    public static void printInfo(String message) {
        LOGGER.info(message);
    }

    public static void printWarning(String message) {
        LOGGER.warning(message);
    }

    public static void printError(String message) {
        LOGGER.severe(message);
    }

    public static void printError(String message, Throwable throwable) {
        LOGGER.log(Level.SEVERE, message, throwable);
    }
}