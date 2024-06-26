package com.thefishnextdoor.tasks.event;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.inventory.ItemStack;

import com.thefishnextdoor.tasks.player.PlayerProfile;
import com.thefishnextdoor.tasks.task.TriggerType;

public class ManipulateArmorstand implements Listener {
        
    @EventHandler
    public void onManipulateArmorstand(PlayerArmorStandManipulateEvent event) {
        Player player = event.getPlayer();
        PlayerProfile playerProfile = PlayerProfile.get(player);
        Entity armorstand = event.getRightClicked();
        ItemStack armorStandItem = event.getArmorStandItem();
        ItemStack playerItem = event.getPlayerItem();
        ItemStack item = armorStandItem != null ? armorStandItem : playerItem;
        playerProfile.triggerTasks(TriggerType.MANIPULATE_ARMORSTAND, armorstand.getLocation(), armorstand, item, null, 1);
    }
}