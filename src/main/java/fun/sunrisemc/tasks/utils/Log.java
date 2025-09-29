package fun.sunrisemc.tasks.utils;

import java.util.logging.Logger;

import org.checkerframework.checker.nullness.qual.NonNull;

import fun.sunrisemc.tasks.TasksPlugin;

public class Log {

    public static void info(@NonNull String message) {
        getLogger().info(message);
    }

    public static void warning(@NonNull String message) {
        getLogger().warning(message);
    }

    public static void severe(@NonNull String message) {
        getLogger().severe(message);
    }

    private static Logger getLogger() {
        return TasksPlugin.getInstance().getLogger();
    }
}