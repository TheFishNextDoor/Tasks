package fun.sunrisemc.tasks.event;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEntityEvent;
import org.bukkit.inventory.ItemStack;

import org.jetbrains.annotations.NotNull;

import fun.sunrisemc.tasks.player.PlayerProfile;
import fun.sunrisemc.tasks.player.PlayerProfileManager;
import fun.sunrisemc.tasks.task.TriggerType;

public class BucketEntity implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onBucketEntity(@NotNull PlayerBucketEntityEvent event) {
        // Get player
        Player player = event.getPlayer();

        // Get the player's profile
        PlayerProfile playerProfile = PlayerProfileManager.get(player);

        // Get the entity being captured
        Entity entity = event.getEntity();

        // Get the location of the entity
        Location entityLocation = entity.getLocation();

        // Get the block at the entity's location
        Block block = entityLocation.getBlock();

        // Get the bucket item
        ItemStack bucket = event.getEntityBucket();

        // Trigger tasks
        playerProfile.triggerTasks(TriggerType.BUCKET_ENTITY, entityLocation, entity, bucket, block, 1);
    }
}