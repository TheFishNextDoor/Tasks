package fun.sunrisemc.tasks.repeating_task;

import org.bukkit.Bukkit;

import fun.sunrisemc.tasks.TasksPlugin;
import fun.sunrisemc.tasks.player.PlayerProfileManager;

public class AutoSaveTask {

    private static final int INTERVAL = 20 * 60 * 5; // 5 minutes

    private static int id = -1;

    public static void start() {
        if (id != -1) {
            return;
        }
        id = Bukkit.getScheduler().runTaskTimerAsynchronously(TasksPlugin.getInstance(), () -> {
            PlayerProfileManager.saveAll();
        }, INTERVAL, INTERVAL).getTaskId();
    }

    public static void stop() {
        if (id == -1) {
            return;
        }
        Bukkit.getScheduler().cancelTask(id);
        id = -1;
    }
}