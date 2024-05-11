package com.thefishnextdoor.tasks.event;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

import com.thefishnextdoor.tasks.player.PlayerProfile;
import com.thefishnextdoor.tasks.task.TriggerType;

public class CraftItem implements Listener {

    @EventHandler
    public void onCraftItem(CraftItemEvent event) {
        Player player = (Player) event.getWhoClicked();
        PlayerProfile playerProfile = PlayerProfile.get(player);
        Block block = event.getInventory().getLocation().getBlock();
        ItemStack item = event.getCurrentItem();
        int amount = item.getAmount();
        playerProfile.triggerTasks(TriggerType.CRAFT_ITEM, null, item, block, amount);
    }
}
