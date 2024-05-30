package com.thefishnextdoor.tasks.event;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import org.bukkit.inventory.ItemStack;

import com.thefishnextdoor.tasks.player.PlayerProfile;
import com.thefishnextdoor.tasks.task.TriggerType;

public class FurnaceExtract implements Listener {

    @EventHandler
    public void onFurnaceExtract(FurnaceExtractEvent event) {
        Player player = event.getPlayer();
        PlayerProfile playerProfile = PlayerProfile.get(player);
        ItemStack item = new ItemStack(event.getItemType(), event.getItemAmount());
        Block block = event.getBlock();
        playerProfile.triggerTasks(TriggerType.SMELT_ITEM, block.getLocation(), player, item, block, item.getAmount());
    }
}