package com.thefishnextdoor.tasks.scheduler;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import com.thefishnextdoor.tasks.TasksPlugin;
import com.thefishnextdoor.tasks.player.PlayerProfile;
import com.thefishnextdoor.tasks.task.TriggerType;
import com.thefishnextdoor.tasks.utils.InventoryUtils;

public class TimerTrigger {

    private static int id = -1;

    public static void start() {
        if (id != -1) {
            return;
        }
        id = Bukkit.getScheduler().scheduleSyncRepeatingTask(TasksPlugin.getInstance(), () -> {
            Bukkit.getOnlinePlayers().forEach(player -> {
                PlayerProfile playerProfile = PlayerProfile.get(player);
                ItemStack hand = InventoryUtils.getItemInHand(player);
                playerProfile.triggerTasks(TriggerType.TIMER, player.getLocation(), player, hand, null, 1);
            });
        }, 20 * 1, 20 * 1);
    }

    public static void stop() {
        if (id == -1) {
            return;
        }
        Bukkit.getScheduler().cancelTask(id);
        id = -1;
    }
}