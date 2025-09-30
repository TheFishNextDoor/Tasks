package fun.sunrisemc.tasks.event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

import fun.sunrisemc.tasks.player.PlayerProfile;
import fun.sunrisemc.tasks.player.PlayerProfileManager;
import fun.sunrisemc.tasks.task.TriggerType;
import fun.sunrisemc.tasks.utils.PlayerUtils;

public class Teleport implements Listener {
    
        @EventHandler
        public void onTeleport(PlayerTeleportEvent event) {
            Player player = event.getPlayer();
            PlayerProfile playerProfile = PlayerProfileManager.get(player);
            ItemStack hand = PlayerUtils.getItemInHand(player);
            playerProfile.triggerTasks(TriggerType.TELEPORT_TO, event.getTo(), player, hand, null, 1);
            playerProfile.triggerTasks(TriggerType.TELEPORT_FROM, event.getFrom(), player, hand, null, 1);
        }
}