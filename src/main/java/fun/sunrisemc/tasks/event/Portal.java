package fun.sunrisemc.tasks.event;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.inventory.ItemStack;

import org.jetbrains.annotations.NotNull;

import fun.sunrisemc.tasks.player.PlayerProfile;
import fun.sunrisemc.tasks.player.PlayerProfileManager;
import fun.sunrisemc.tasks.task.TriggerType;
import fun.sunrisemc.tasks.utils.PlayerUtils;

public class Portal implements Listener {
    
    @EventHandler(ignoreCancelled = true)
    public void onPortal(@NotNull PlayerPortalEvent event) {
        // Get the player
        Player player = event.getPlayer();

        // Get the player profile
        PlayerProfile playerProfile = PlayerProfileManager.get(player);

        // Get the item in the player's hand
        ItemStack item = PlayerUtils.getItemInHand(player);

        // Get the location portaled to and from
        Location locationPortaledTo = event.getTo();
        Location locationPortaledFrom = event.getFrom();

        // Check if there is a location portaled to
        if (locationPortaledTo != null) {
            // Get the block at the location portaled to
            Block blockPortaledTo = locationPortaledTo.getBlock();

            // Trigger tasks for portaling to
            playerProfile.triggerTasks(TriggerType.PORTAL_TO, locationPortaledTo, player, item, blockPortaledTo, 1);
        }
        
        // Get the block at the location portaled from
        Block blockPortaledFrom = locationPortaledFrom.getBlock();

        // Trigger tasks for portaling from
        playerProfile.triggerTasks(TriggerType.PORTAL_FROM, locationPortaledFrom, player, item, blockPortaledFrom, 1);
    }
}