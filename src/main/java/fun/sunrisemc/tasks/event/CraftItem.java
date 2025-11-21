package fun.sunrisemc.tasks.event;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

import org.jetbrains.annotations.NotNull;

import fun.sunrisemc.tasks.player.PlayerProfile;
import fun.sunrisemc.tasks.player.PlayerProfileManager;
import fun.sunrisemc.tasks.task.TriggerType;

public class CraftItem implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onCraftItem(@NotNull CraftItemEvent event) {
        // Get player
        HumanEntity human = event.getWhoClicked();
        if (!(human instanceof Player)) {
            return;
        }
        Player player = (Player) human;

        // Get the player's profile
        PlayerProfile playerProfile = PlayerProfileManager.get(player);

        // Get crafting inventory
        CraftingInventory craftingInventory = event.getInventory();

        // Get result item
        ItemStack resultItem = craftingInventory.getResult();
        if (resultItem == null || resultItem.getType().isAir()) {
            return;
        }

        // Get the amount crafted
        int amountCrafted = resultItem.getAmount();
        if (event.getClick().isShiftClick()) {
            int craftResultAmount = resultItem.getAmount();

            // Find the smallest stack size of the crafting items
            int smallestCraftingItemStack = resultItem.getMaxStackSize() + 1000;
            for (ItemStack craftingItem : craftingInventory.getContents()) {
                // Skip null or air items
                if (craftingItem == null || craftingItem.getType().isAir()) {
                    continue;
                }

                // Skip the crafted item itself
                Material craftingMaterial = craftingItem.getType();
                if (craftingMaterial.equals(resultItem.getType())) {
                    continue;
                }

                if (smallestCraftingItemStack > craftingItem.getAmount()) {
                    smallestCraftingItemStack = craftingItem.getAmount();
                }
            }

            amountCrafted = smallestCraftingItemStack * craftResultAmount;
        }

        // Get the location of the crafting inventory or default to player location if crafting inventory has no location
        Location location = craftingInventory.getLocation();
        if (location == null) {
            location = player.getLocation();
        }

        // Get the block at the location
        Block craftingBlock = location != null ? location.getBlock() : null;

        // Get the crafted item
        ItemStack craftedItem = event.getCurrentItem();
        
        // Trigger tasks
        playerProfile.triggerTasks(TriggerType.CRAFT_ITEM, location, player, craftedItem, craftingBlock, amountCrafted);
    }
}