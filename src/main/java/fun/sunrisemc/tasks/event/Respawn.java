package fun.sunrisemc.tasks.event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import fun.sunrisemc.tasks.player.PlayerProfile;
import fun.sunrisemc.tasks.player.PlayerProfileManager;
import fun.sunrisemc.tasks.task.TriggerType;
import fun.sunrisemc.tasks.utils.PlayerUtils;

public class Respawn implements Listener {

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        PlayerProfile playerProfile = PlayerProfileManager.get(player);
        ItemStack item = PlayerUtils.getItemInHand(player);
        playerProfile.triggerTasks(TriggerType.RESPAWN, event.getRespawnLocation(), player, item, null, 1);
    }
}