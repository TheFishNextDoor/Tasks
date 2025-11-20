package fun.sunrisemc.tasks.event;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.inventory.ItemStack;

import org.jetbrains.annotations.NotNull;

import fun.sunrisemc.tasks.player.PlayerProfile;
import fun.sunrisemc.tasks.player.PlayerProfileManager;
import fun.sunrisemc.tasks.task.TriggerType;

public class BucketFill implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onBucketFill(@NotNull PlayerBucketFillEvent event) {
        // Get player
        Player player = event.getPlayer();

        // Get the player's profile
        PlayerProfile playerProfile = PlayerProfileManager.get(player);

        // Get the bucket
        ItemStack bucket = event.getItemStack();

        // Get the block clicked
        Block blockClicked = event.getBlockClicked();

        // Get the block location
        Location blockLocation = blockClicked.getLocation();

        // Trigger tasks
        playerProfile.triggerTasks(TriggerType.FILL_BUCKET, blockLocation, player, bucket, blockClicked, 1);
    }
}