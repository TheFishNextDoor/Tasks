package com.thefishnextdoor.tasks.event;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.inventory.ItemStack;

import com.thefishnextdoor.tasks.player.PlayerProfile;
import com.thefishnextdoor.tasks.task.TriggerType;
import com.thefishnextdoor.tasks.utils.InventoryUtils;

public class BedLeave implements Listener {

    @EventHandler
    public void onBedLeave(PlayerBedLeaveEvent event) {
        Player player = event.getPlayer();
        PlayerProfile playerProfile = PlayerProfile.get(player);
        Block block = event.getBed();
        ItemStack item = InventoryUtils.getItemInHand(player);
        playerProfile.triggerTasks(TriggerType.LEAVE_BED, block.getLocation(), player, item, block, 1);
    }
}