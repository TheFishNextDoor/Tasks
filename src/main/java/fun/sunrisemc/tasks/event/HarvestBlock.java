package fun.sunrisemc.tasks.event;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerHarvestBlockEvent;
import org.bukkit.inventory.ItemStack;

import org.jetbrains.annotations.NotNull;

import fun.sunrisemc.tasks.player.PlayerProfile;
import fun.sunrisemc.tasks.player.PlayerProfileManager;
import fun.sunrisemc.tasks.task.TriggerType;
import fun.sunrisemc.tasks.utils.PlayerUtils;

public class HarvestBlock implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onPlayerHarvest(@NotNull PlayerHarvestBlockEvent event) {
        // Get the player
        Player player = event.getPlayer();

        // Get the player's profile
        PlayerProfile playerProfile = PlayerProfileManager.get(player);

        // Get the block harvested
        Block blockHarvested = event.getHarvestedBlock();

        // Get the location of the harvested block
        Location location = blockHarvested.getLocation();

        // Get the item in the player's hand
        ItemStack itemInHand = PlayerUtils.getItemInHand(player).orElse(null);

        // Trigger harvest block tasks
        playerProfile.triggerTasks(TriggerType.HARVEST_BLOCK, location, player, itemInHand, blockHarvested, 1);

        // Trigger harvest item tasks for each item harvested
        for (ItemStack item : event.getItemsHarvested()) {
            // Get amount harvested
            int amountHarvested = item.getAmount();

            // Trigger harvest item tasks
            playerProfile.triggerTasks(TriggerType.HARVEST_ITEM, location, player, item, blockHarvested, amountHarvested);
        }
    }
}