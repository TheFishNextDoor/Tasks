package com.thefishnextdoor.tasks.event;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupArrowEvent;

import com.thefishnextdoor.tasks.player.PlayerProfile;
import com.thefishnextdoor.tasks.task.TriggerType;

public class PickupArrow implements Listener {

    @EventHandler
    public void onPickupArrow(PlayerPickupArrowEvent event) {
        Player player = event.getPlayer();
        PlayerProfile playerProfile = PlayerProfile.get(player);
        Entity arrow = event.getArrow();
        playerProfile.triggerTasks(TriggerType.PICKUP_ARROW, arrow.getLocation(), arrow, null, null, 1);
    }
}