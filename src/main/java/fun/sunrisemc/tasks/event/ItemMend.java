package fun.sunrisemc.tasks.event;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemMendEvent;
import org.bukkit.inventory.ItemStack;

import org.jetbrains.annotations.NotNull;

import fun.sunrisemc.tasks.player.PlayerProfile;
import fun.sunrisemc.tasks.player.PlayerProfileManager;
import fun.sunrisemc.tasks.task.TriggerType;

public class ItemMend implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onItemMend(@NotNull PlayerItemMendEvent event) {
        // Get the player
        Player player = event.getPlayer();

        // Get the player's profile
        PlayerProfile playerProfile = PlayerProfileManager.get(player);

        // Get the players location
        Location playerLocation = player.getLocation();

        // Get the item being mended
        ItemStack item = event.getItem();

        // Get the repair amount
        int repairAmount = event.getRepairAmount();

        // Trigger tasks
        playerProfile.triggerTasks(TriggerType.MEND_ITEM, playerLocation, player, item, null, repairAmount);
    }
}