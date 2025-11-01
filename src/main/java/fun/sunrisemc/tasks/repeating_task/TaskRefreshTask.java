package fun.sunrisemc.tasks.repeating_task;

import org.bukkit.Bukkit;

import fun.sunrisemc.tasks.TasksPlugin;
import fun.sunrisemc.tasks.player.PlayerProfileManager;

public class TaskRefreshTask {

    private static final int INTERVAL = 20 * 5; // 5 seconds

    private static int id = -1;

    public static void start() {
        if (id != -1) {
            return;
        }
        id = Bukkit.getScheduler().scheduleSyncRepeatingTask(TasksPlugin.getInstance(), () -> {
            PlayerProfileManager.refreshAllTasks();
        }, INTERVAL, INTERVAL);
    }

    public static void stop() {
        if (id == -1) {
            return;
        }
        Bukkit.getScheduler().cancelTask(id);
        id = -1;
    }
}