package fun.sunrisemc.tasks.event;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import org.bukkit.inventory.ItemStack;

import org.jetbrains.annotations.NotNull;

import fun.sunrisemc.tasks.player.PlayerProfile;
import fun.sunrisemc.tasks.player.PlayerProfileManager;
import fun.sunrisemc.tasks.task.TriggerType;

public class FurnaceExtract implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onFurnaceExtract(@NotNull FurnaceExtractEvent event) {
        // Get the player
        Player player = event.getPlayer();

        // Get the player's profile
        PlayerProfile playerProfile = PlayerProfileManager.get(player);

        // Get the amount extracted
        int amountExtracted = event.getItemAmount();

        // Get the item being extracted
        ItemStack itemExtracted = new ItemStack(event.getItemType(), amountExtracted);

        // Get the furnace block
        Block furnace = event.getBlock();

        // Get the furnace location
        Location furnaceLocation = furnace.getLocation();

        // Trigger tasks
        playerProfile.triggerTasks(TriggerType.SMELT_ITEM, furnaceLocation, player, itemExtracted, furnace, amountExtracted);
    }
}