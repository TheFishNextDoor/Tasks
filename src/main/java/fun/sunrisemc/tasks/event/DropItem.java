package fun.sunrisemc.tasks.event;

import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import org.jetbrains.annotations.NotNull;

import fun.sunrisemc.tasks.player.PlayerProfile;
import fun.sunrisemc.tasks.player.PlayerProfileManager;
import fun.sunrisemc.tasks.task.TriggerType;

public class DropItem implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onDropItem(@NotNull PlayerDropItemEvent event) {
        // Get player
        Player player = event.getPlayer();

        // Get the player's profile
        PlayerProfile playerProfile = PlayerProfileManager.get(player);

        // Get dropped item
        Item droppedItem = event.getItemDrop();

        // Get drop location
        Location dropLocation = droppedItem.getLocation();

        // Get dropped item stack
        ItemStack droppedItemStack = droppedItem.getItemStack();

        // Get amount dropped
        int amountDropped = droppedItemStack.getAmount();

        // Trigger tasks
        playerProfile.triggerTasks(TriggerType.DROP_ITEM, dropLocation, droppedItem, droppedItemStack, null, amountDropped);
    }
}