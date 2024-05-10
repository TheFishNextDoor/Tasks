package com.thefishnextdoor.tasks.event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.inventory.ItemStack;

import com.thefishnextdoor.tasks.player.PlayerProfile;
import com.thefishnextdoor.tasks.task.TriggerType;

public class EnchantItem implements Listener {

    @EventHandler
    public void onEnchantItem(EnchantItemEvent event) {
        Player player = event.getEnchanter();
        ItemStack item = event.getItem();
        PlayerProfile playerProfile = PlayerProfile.get(player);
        playerProfile.triggerTasks(TriggerType.ENCHANT_ITEM, null, item, null, 1);
    }
}