package com.thefishnextdoor.tasks.scheduler;

import org.bukkit.Bukkit;

import com.thefishnextdoor.tasks.TasksPlugin;
import com.thefishnextdoor.tasks.player.PlayerProfile;

public class AutoSave {

    private static int id = -1;

    public static void start() {
        if (id != -1) {
            return;
        }
        Bukkit.getScheduler().runTaskTimerAsynchronously(TasksPlugin.getInstance(), () -> {
            PlayerProfile.saveAll();
        }, 20 * 60 * 5, 20 * 60 * 5);
    }

    public static void stop() {
        if (id == -1) {
            return;
        }
        Bukkit.getScheduler().cancelTask(id);
        id = -1;
    }
}
