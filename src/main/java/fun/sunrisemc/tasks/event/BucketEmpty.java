package fun.sunrisemc.tasks.event;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.inventory.ItemStack;

import org.jetbrains.annotations.NotNull;

import fun.sunrisemc.tasks.player.PlayerProfile;
import fun.sunrisemc.tasks.player.PlayerProfileManager;
import fun.sunrisemc.tasks.task.TriggerType;

public class BucketEmpty implements Listener {
    
    @EventHandler(ignoreCancelled = true)
    public void onBucketEmpty(@NotNull PlayerBucketEmptyEvent event) {
        // Get player
        Player player = event.getPlayer();

        // Get the player's profile
        PlayerProfile playerProfile = PlayerProfileManager.get(player);

        // Get the bucket item
        ItemStack bucket = event.getItemStack();

        // Get the block where the bucket was emptied
        Block block = event.getBlock();

        // Get the location of the block
        Location emptyLocation = block.getLocation();

        // Trigger tasks
        playerProfile.triggerTasks(TriggerType.EMPTY_BUCKET, emptyLocation, player, bucket, block, 1);
    }
}