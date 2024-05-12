package com.thefishnextdoor.tasks.event;

import org.bukkit.Location;
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
        Location location = event.getInventory().getLocation();
        Block block = null;
        if (location != null) {
            block = location.getBlock();
        }
        ItemStack item = event.getCurrentItem();
        int amount = item.getAmount();
        playerProfile.triggerTasks(TriggerType.CRAFT_ITEM, player.getLocation(), player, item, block, amount);
    }
}
