package fun.sunrisemc.tasks.event;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.inventory.ItemStack;

import org.jetbrains.annotations.NotNull;

import fun.sunrisemc.tasks.player.PlayerProfile;
import fun.sunrisemc.tasks.player.PlayerProfileManager;
import fun.sunrisemc.tasks.task.TriggerType;

public class EnchantItem implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onEnchantItem(@NotNull EnchantItemEvent event) {
        // Get player
        Player enchanter = event.getEnchanter();

        // Get the player's profile
        PlayerProfile playerProfile = PlayerProfileManager.get(enchanter);

        // Get item being enchanted
        ItemStack enchantedItem = event.getItem();

        // Get the enchanting table block
        Block enchantingTable = event.getEnchantBlock();

        // Get the location of the enchanting table
        Location enchantingTableLocation = enchantingTable.getLocation();

        // Trigger tasks
        playerProfile.triggerTasks(TriggerType.ENCHANT_ITEM, enchantingTableLocation, enchanter, enchantedItem, enchantingTable, 1);
    }
}