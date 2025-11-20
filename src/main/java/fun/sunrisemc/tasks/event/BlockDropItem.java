package fun.sunrisemc.tasks.event;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.ItemStack;

import org.jetbrains.annotations.NotNull;

import fun.sunrisemc.tasks.player.PlayerProfile;
import fun.sunrisemc.tasks.player.PlayerProfileManager;
import fun.sunrisemc.tasks.task.TriggerType;

public class BlockDropItem implements Listener {
        
    @EventHandler(ignoreCancelled = true)
    public void onBlockDropItem(@NotNull BlockDropItemEvent event) {
        // Get player
        Player player = event.getPlayer();

        // Get the player's profile
        PlayerProfile playerProfile = PlayerProfileManager.get(player);

        // Get block that dropped the items
        Block block = event.getBlock();

        // Get the location of the block
        Location blockLocation = block.getLocation();

        // Trigger tasks for each dropped item
        event.getItems().forEach(droppedItem -> {
            ItemStack droppedItemStack = droppedItem.getItemStack();
            playerProfile.triggerTasks(TriggerType.BREAK_BLOCK_DROP_ITEM, blockLocation, player, droppedItemStack, block, droppedItemStack.getAmount());
        });
    }
}