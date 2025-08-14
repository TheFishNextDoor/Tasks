package com.thefishnextdoor.tasks.event;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import com.thefishnextdoor.tasks.player.PlayerProfile;
import com.thefishnextdoor.tasks.task.TriggerType;
import com.thefishnextdoor.tasks.utils.InventoryUtils;

public class EntityDeath implements Listener {

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        Player killer = entity.getKiller();
        ItemStack hand = null;

        if (killer != null) {
            hand = InventoryUtils.getItemInHand(killer);
            PlayerProfile playerProfile = PlayerProfile.get(killer);
            playerProfile.triggerTasks(TriggerType.KILL_ENTITY, entity.getLocation(), entity, hand, null, 1);
            for (ItemStack item : event.getDrops()) {
                playerProfile.triggerTasks(TriggerType.KILL_ENTITY_DROP_ITEM, entity.getLocation(), entity, item, null, item.getAmount());
            }
        }

        if (entity instanceof Player) {
            Player player = (Player) entity;
            PlayerProfile playerProfile = PlayerProfile.get(player);
            if (killer != null) {
                hand = InventoryUtils.getItemInHand(killer);
            }
            playerProfile.triggerTasks(TriggerType.DEATH, player.getLocation(), player, hand, null, 1);
        }
    }
}