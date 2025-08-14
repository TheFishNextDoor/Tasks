package com.thefishnextdoor.tasks.utils;

import java.util.logging.Logger;

import com.thefishnextdoor.tasks.TasksPlugin;

public class Debug {

    public static void logInfo(String message) {
        getLogger().info(message);
    }

    public static void logWarning(String message) {
        getLogger().warning(message);
    }

    public static void logSevere(String message) {
        getLogger().severe(message);
    }

    private static Logger getLogger() {
        return TasksPlugin.getInstance().getLogger();
    }
}