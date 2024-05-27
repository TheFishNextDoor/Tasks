package com.thefishnextdoor.tasks.event;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

import com.thefishnextdoor.tasks.player.PlayerProfile;
import com.thefishnextdoor.tasks.task.TriggerType;

public class CraftItem implements Listener {

    @EventHandler
    public void onCraftItem(CraftItemEvent event) {
        HumanEntity human = event.getWhoClicked();
        if (!(human instanceof Player)) {
            return;
        }
        Player player = (Player) human;
        CraftingInventory inventory = event.getInventory();
        ItemStack craftedItem = inventory.getResult();
        ClickType clickType = event.getClick();
        int realAmount = craftedItem.getAmount();
        if (clickType.isShiftClick()) {
            int lowerAmount = craftedItem.getMaxStackSize() + 1000;
            for (ItemStack actualItem : inventory.getContents()) {
                if (actualItem != null && !actualItem.getType().isAir() && lowerAmount > actualItem.getAmount() && !actualItem.getType().equals(craftedItem.getType())) {
                    lowerAmount = actualItem.getAmount();
                }
            }
            realAmount = lowerAmount * craftedItem.getAmount();
        }
        Location location = inventory.getLocation();
        Block block = location != null ? location.getBlock() : null;
        ItemStack item = event.getCurrentItem();
        PlayerProfile playerProfile = PlayerProfile.get(player);
        playerProfile.triggerTasks(TriggerType.CRAFT_ITEM, player.getLocation(), player, item, block, realAmount);
    }
}