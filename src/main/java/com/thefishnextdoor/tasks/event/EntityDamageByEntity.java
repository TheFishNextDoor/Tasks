package com.thefishnextdoor.tasks.event;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import com.thefishnextdoor.tasks.player.PlayerProfile;
import com.thefishnextdoor.tasks.task.TriggerType;
import com.thefishnextdoor.tasks.utils.InventoryUtils;

public class EntityDamageByEntity implements Listener {
    
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        Entity damaged = event.getEntity();
        if (damager instanceof Player) {
            Player player = (Player) damager;
            PlayerProfile playerProfile = PlayerProfile.get(player);
            ItemStack item = InventoryUtils.getItemInHand(player);
            int damage = (int) event.getDamage();
            playerProfile.triggerTasks(TriggerType.DAMAGE_ENTITY, damaged.getLocation(), damaged, item, null, damage);
        }
    }
}