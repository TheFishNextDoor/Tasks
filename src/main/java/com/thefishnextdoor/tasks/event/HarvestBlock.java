package com.thefishnextdoor.tasks.event;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerHarvestBlockEvent;
import org.bukkit.inventory.ItemStack;

import com.thefishnextdoor.tasks.player.PlayerProfile;
import com.thefishnextdoor.tasks.task.TriggerType;
import com.thefishnextdoor.tasks.toolkit.InventoryTools;

public class HarvestBlock implements Listener {

    @EventHandler
    public void onBlockBreak(PlayerHarvestBlockEvent event) {
        Player player = event.getPlayer();
        PlayerProfile playerProfile = PlayerProfile.get(player);
        Block block = event.getHarvestedBlock();
        ItemStack item = InventoryTools.getItemInHand(player);
        int count = 0;
        for (ItemStack harvestedItem : event.getItemsHarvested()) {
            count += harvestedItem.getAmount();
        }
        playerProfile.triggerTasks(TriggerType.HARVEST_BLOCK, null, item, block, count);
    }
}