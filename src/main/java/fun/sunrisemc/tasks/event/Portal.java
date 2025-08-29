package fun.sunrisemc.tasks.event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.inventory.ItemStack;

import fun.sunrisemc.tasks.player.PlayerProfile;
import fun.sunrisemc.tasks.player.PlayerProfileManager;
import fun.sunrisemc.tasks.task.TriggerType;

public class Portal implements Listener {
    
    @EventHandler
    public void onPortal(PlayerPortalEvent event) {
        Player player = event.getPlayer();
        PlayerProfile playerProfile = PlayerProfileManager.get(player);
        ItemStack item = player.getInventory().getItemInMainHand();
        playerProfile.triggerTasks(TriggerType.PORTAL_TO, event.getTo(), player, item, null, 1);
        playerProfile.triggerTasks(TriggerType.PORTAL_FROM, event.getFrom(), player, item, null, 1);
    }
}