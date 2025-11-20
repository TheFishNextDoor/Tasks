package fun.sunrisemc.tasks.event;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRiptideEvent;
import org.bukkit.inventory.ItemStack;

import org.jetbrains.annotations.NotNull;

import fun.sunrisemc.tasks.player.PlayerProfile;
import fun.sunrisemc.tasks.player.PlayerProfileManager;
import fun.sunrisemc.tasks.task.TriggerType;

public class Riptide implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onRiptide(@NotNull PlayerRiptideEvent event) {
        // Get the player
        Player player = event.getPlayer();

        // Get the player's profile
        PlayerProfile playerProfile = PlayerProfileManager.get(player);

        // Get the players location
        Location location = player.getLocation();
        
        // Get the block at the player's location
        Block blockAtLocation = location.getBlock();

        // Get the trident
        ItemStack trident = event.getItem();

        // Trigger tasks
        playerProfile.triggerTasks(TriggerType.RIPTIDE, location, player, trident, blockAtLocation, 1);
    }
}