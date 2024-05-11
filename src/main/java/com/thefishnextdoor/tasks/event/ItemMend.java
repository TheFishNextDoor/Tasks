package com.thefishnextdoor.tasks.event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemMendEvent;
import org.bukkit.inventory.ItemStack;

import com.thefishnextdoor.tasks.player.PlayerProfile;
import com.thefishnextdoor.tasks.task.TriggerType;

public class ItemMend implements Listener {

    @EventHandler
    public void onItemMend(PlayerItemMendEvent event) {
        Player player = event.getPlayer();
        PlayerProfile playerProfile = PlayerProfile.get(player);
        ItemStack item = event.getItem();
        playerProfile.triggerTasks(TriggerType.MEND_ITEM, player.getLocation(), player, item, null, event.getRepairAmount());
    }
}