package com.thefishnextdoor.tasks.event;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import com.thefishnextdoor.tasks.player.PlayerProfile;
import com.thefishnextdoor.tasks.task.TriggerType;
import com.thefishnextdoor.tasks.toolkit.InventoryTools;

public class BlockPlace implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        PlayerProfile playerProfile = PlayerProfile.get(player);
        Block block = event.getBlock();
        ItemStack item = InventoryTools.getItemInHand(player);
        playerProfile.triggerTasks(TriggerType.PLACE_BLOCK, null, item, block, 1);
    }
}
