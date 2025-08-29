package fun.sunrisemc.tasks.scheduler;

import org.bukkit.Bukkit;

import fun.sunrisemc.tasks.TasksPlugin;
import fun.sunrisemc.tasks.player.PlayerProfileManager;

public class AutoSave {

    private static int id = -1;

    public static void start() {
        if (id != -1) {
            return;
        }
        Bukkit.getScheduler().runTaskTimerAsynchronously(TasksPlugin.getInstance(), () -> {
            PlayerProfileManager.saveAll();
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