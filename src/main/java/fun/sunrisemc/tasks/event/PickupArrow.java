package fun.sunrisemc.tasks.event;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupArrowEvent;

import org.jetbrains.annotations.NotNull;

import fun.sunrisemc.tasks.player.PlayerProfile;
import fun.sunrisemc.tasks.player.PlayerProfileManager;
import fun.sunrisemc.tasks.task.TriggerType;

public class PickupArrow implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onPickupArrow(@NotNull PlayerPickupArrowEvent event) {
        // Get the player
        Player player = event.getPlayer();

        // Get the player's profile
        PlayerProfile playerProfile = PlayerProfileManager.get(player);

        // Get the arrow entity
        AbstractArrow arrow = event.getArrow();

        // Get the arrow location
        Location arrowLocation = arrow.getLocation();

        // Get the block the arrow is attached to (if any)
        Block attachedBlock = arrow.getAttachedBlock();

        // Trigger tasks
        playerProfile.triggerTasks(TriggerType.PICKUP_ARROW, arrowLocation, arrow, null, attachedBlock, 1);
    }
}