package com.thefishnextdoor.tasks.event;

import org.bukkit.Location;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.inventory.ItemStack;

import com.thefishnextdoor.tasks.player.PlayerProfile;
import com.thefishnextdoor.tasks.task.TriggerType;
import com.thefishnextdoor.tasks.toolkit.InventoryTools;

public class EntityTame implements Listener {
    
    @EventHandler
    public void onEntityTame(EntityTameEvent event) {
        AnimalTamer tamer = event.getOwner();
        if (!(tamer instanceof Player)) {
            return;
        }
        Player player = (Player) tamer;
        PlayerProfile playerProfile = PlayerProfile.get(player);
        LivingEntity entity = event.getEntity();
        ItemStack item = InventoryTools.getItemInHand(player);
        Location location = entity.getLocation();
        playerProfile.triggerTasks(TriggerType.TAME_ENTITY, location, entity, item, null, 1);
    }
}