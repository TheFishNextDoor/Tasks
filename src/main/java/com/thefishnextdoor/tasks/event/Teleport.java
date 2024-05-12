package com.thefishnextdoor.tasks.event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

import com.thefishnextdoor.tasks.player.PlayerProfile;
import com.thefishnextdoor.tasks.task.TriggerType;
import com.thefishnextdoor.tasks.toolkit.InventoryTools;

public class Teleport implements Listener {
    
        @EventHandler
        public void onTeleport(PlayerTeleportEvent event) {
            Player player = event.getPlayer();
            PlayerProfile playerProfile = PlayerProfile.get(player);
            ItemStack hand = InventoryTools.getItemInHand(player);
            playerProfile.triggerTasks(TriggerType.TELEPORT_TO, event.getTo(), null, hand, null, 1);
            playerProfile.triggerTasks(TriggerType.TELEPORT_FROM, event.getFrom(), null, hand, null, 1);
        }
}