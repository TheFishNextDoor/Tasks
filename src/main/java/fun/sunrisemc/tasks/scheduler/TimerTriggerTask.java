package fun.sunrisemc.tasks.scheduler;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import fun.sunrisemc.tasks.TasksPlugin;
import fun.sunrisemc.tasks.player.PlayerProfile;
import fun.sunrisemc.tasks.player.PlayerProfileManager;
import fun.sunrisemc.tasks.task.TriggerType;
import fun.sunrisemc.tasks.utils.PlayerUtils;

public class TimerTriggerTask {

    private static final int INTERVAL = 20 * 1; // 1 second

    private static int id = -1;

    public static void start() {
        if (id != -1) {
            return;
        }
        
        id = Bukkit.getScheduler().scheduleSyncRepeatingTask(TasksPlugin.getInstance(), () -> {
            Bukkit.getOnlinePlayers().forEach(player -> {
                if (player == null) {
                    return;
                }

                PlayerProfile playerProfile = PlayerProfileManager.get(player);

                ItemStack itemInHand = PlayerUtils.getItemInHand(player).orElse(null);
                
                playerProfile.triggerTasks(TriggerType.TIMER, player.getLocation(), player, itemInHand, null, 1);
            });
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