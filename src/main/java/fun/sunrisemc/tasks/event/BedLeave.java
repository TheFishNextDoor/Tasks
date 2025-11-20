package fun.sunrisemc.tasks.event;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.inventory.ItemStack;

import org.jetbrains.annotations.NotNull;

import fun.sunrisemc.tasks.player.PlayerProfile;
import fun.sunrisemc.tasks.player.PlayerProfileManager;
import fun.sunrisemc.tasks.task.TriggerType;
import fun.sunrisemc.tasks.utils.PlayerUtils;

public class BedLeave implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onBedLeave(@NotNull PlayerBedLeaveEvent event) {
        // Get player
        Player player = event.getPlayer();

        // Get the player's profile
        PlayerProfile playerProfile = PlayerProfileManager.get(player);

        // Get bed left
        Block leftBed = event.getBed();

        // Get bed location
        Location bedLocation = leftBed.getLocation();

        // Get item in player's hand
        ItemStack itemInHand = PlayerUtils.getItemInHand(player);

        // Trigger tasks
        playerProfile.triggerTasks(TriggerType.LEAVE_BED, bedLocation, player, itemInHand, leftBed, 1);
    }
}