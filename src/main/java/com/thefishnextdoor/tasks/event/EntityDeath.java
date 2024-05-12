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
        ItemStack item = null;

        if (killer != null) {
            item = InventoryTools.getItemInHand(killer);
            PlayerProfile playerProfile = PlayerProfile.get(killer);
            playerProfile.triggerTasks(TriggerType.KILL_ENTITY, entity.getLocation(), entity, item, null, 1);
        }

        if (entity instanceof Player) {
            Player player = (Player) entity;
            PlayerProfile playerProfile = PlayerProfile.get(player);
            if (killer != null) {
                item = InventoryTools.getItemInHand(killer);
            }
            playerProfile.triggerTasks(TriggerType.DEATH, player.getLocation(), null, item, null, 1);
        }
    }
}