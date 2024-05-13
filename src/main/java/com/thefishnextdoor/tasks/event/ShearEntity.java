package com.thefishnextdoor.tasks.event;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.inventory.ItemStack;

import com.thefishnextdoor.tasks.player.PlayerProfile;
import com.thefishnextdoor.tasks.task.TriggerType;

public class ShearEntity implements Listener {
    
        @EventHandler
        public void onShearEntity(PlayerShearEntityEvent event) {
            Player player = event.getPlayer();
            PlayerProfile playerProfile = PlayerProfile.get(player);
            Entity entity = event.getEntity();
            ItemStack item = event.getItem();
            playerProfile.triggerTasks(TriggerType.SHEAR_ENTITY, entity.getLocation(), entity, item, null, 1);
            playerProfile.triggerTasks(TriggerType.SHEAR_ENTITY_DROP_ITEM, entity.getLocation(), entity, item, null, item.getAmount());
        }
}