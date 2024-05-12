package com.thefishnextdoor.tasks.event;

import org.bukkit.block.Block;
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
        PlayerProfile playerProfile = PlayerProfile.get(player);
        ItemStack item = event.getItem();
        Block block = event.getEnchantBlock();
        playerProfile.triggerTasks(TriggerType.ENCHANT_ITEM, block.getLocation(), null, item, block, 1);
    }
}