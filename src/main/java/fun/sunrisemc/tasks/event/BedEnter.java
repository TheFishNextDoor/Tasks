package fun.sunrisemc.tasks.event;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.inventory.ItemStack;

import org.jetbrains.annotations.NotNull;

import fun.sunrisemc.tasks.player.PlayerProfile;
import fun.sunrisemc.tasks.player.PlayerProfileManager;
import fun.sunrisemc.tasks.task.TriggerType;
import fun.sunrisemc.tasks.utils.PlayerUtils;

public class BedEnter implements Listener {
    
    @EventHandler(ignoreCancelled = true)
    public void onBedEnter(@NotNull PlayerBedEnterEvent event) {
        // Get player
        Player player = event.getPlayer();

        // Get player profile
        PlayerProfile playerProfile = PlayerProfileManager.get(player);

        // Get bed
        Block bed = event.getBed();

        // Get item in players hand
        ItemStack item = PlayerUtils.getItemInHand(player);

        // Trigger tasks
        playerProfile.triggerTasks(TriggerType.ENTER_BED, bed.getLocation(), player, item, bed, 1);
    }
}