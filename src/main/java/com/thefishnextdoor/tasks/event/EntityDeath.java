package com.thefishnextdoor.tasks.event;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import com.thefishnextdoor.tasks.player.PlayerProfile;
import com.thefishnextdoor.tasks.task.TriggerType;
import com.thefishnextdoor.tasks.toolkit.InventoryTools;

public class EntityDeath implements Listener {

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        Player killer = entity.getKiller();
        if (killer != null) {
            PlayerProfile playerProfile = PlayerProfile.get(killer);
            ItemStack item = InventoryTools.getItemInHand(killer);
            playerProfile.triggerTasks(TriggerType.KILL_ENTITY, entity, item, null, 1);
        }
    }
}