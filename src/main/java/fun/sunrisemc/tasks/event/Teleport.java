package fun.sunrisemc.tasks.event;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

import org.jetbrains.annotations.NotNull;

import fun.sunrisemc.tasks.player.PlayerProfile;
import fun.sunrisemc.tasks.player.PlayerProfileManager;
import fun.sunrisemc.tasks.task.TriggerType;
import fun.sunrisemc.tasks.utils.PlayerUtils;

public class Teleport implements Listener {
    
        @EventHandler(ignoreCancelled = true)
        public void onTeleport(@NotNull PlayerTeleportEvent event) {
            // Get the player
            Player player = event.getPlayer();

            // Get the player's profile
            PlayerProfile playerProfile = PlayerProfileManager.get(player);

            // Get the item in the player's hand
            ItemStack itemInHand = PlayerUtils.getItemInHand(player).orElse(null);

            // Get the location teleported to and from
            Location locationTeleportedTo = event.getTo();
            Location locationTeleportedFrom = event.getFrom();

            // Check if there is a location teleported to
            if (locationTeleportedTo != null) {
                // Get the block teleported to
                Block blockTeleportedTo = locationTeleportedTo.getBlock();

                // Trigger tasks for teleporting to
                playerProfile.triggerTasks(TriggerType.TELEPORT_TO, locationTeleportedTo, player, itemInHand, blockTeleportedTo, 1);
            }

            // Get the block teleported from
            Block blockTeleportedFrom = locationTeleportedFrom.getBlock();

            // Trigger tasks for teleporting from
            playerProfile.triggerTasks(TriggerType.TELEPORT_FROM, locationTeleportedFrom, player, itemInHand, blockTeleportedFrom, 1);
        }
}