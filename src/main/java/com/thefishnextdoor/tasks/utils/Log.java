package com.thefishnextdoor.tasks.utils;

import java.util.logging.Logger;

import com.thefishnextdoor.tasks.TasksPlugin;

public class Log {

    public static void info(String message) {
        getLogger().info(message);
    }

    public static void warning(String message) {
        getLogger().warning(message);
    }

    public static void severe(String message) {
        getLogger().severe(message);
    }

    private static Logger getLogger() {
        return TasksPlugin.getInstance().getLogger();
    }
}